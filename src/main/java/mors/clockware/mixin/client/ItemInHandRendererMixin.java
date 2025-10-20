package mors.clockware.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import mors.clockware.utils.DualWieldClockware;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow protected abstract void renderPlayerArm(PoseStack poseStack, MultiBufferSource buffer, int packedLight, float equippedProgress, float swingProgress, HumanoidArm side);

    @Shadow protected abstract void applyBrushTransform(PoseStack poseStack, float partialTick, HumanoidArm arm, ItemStack stack, Player player, float equippedProgress);

    @Shadow protected abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm hand, float equippedProg);

    @Shadow protected abstract void applyEatTransform(PoseStack poseStack, float partialTick, HumanoidArm arm, ItemStack stack, Player player);

    @Shadow @Final private Minecraft minecraft;

    @Shadow private float oMainHandHeight;

    @Shadow private float mainHandHeight;

    //Dual-wielding logic
    @ModifyArg(method = "renderHandsWithItems", at =
    @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
            ,index = 4)
    private float clockware$modifySwingsForDualwield(float swing, @Local InteractionHand interactionHand, @Local(ordinal = 1) float f){
        if(this.minecraft.player==null)
            return swing;

        if(Clockware_Util.hasDualwieldClockware(this.minecraft.player)){
            return f;
        }

        return swing;
    }

    @ModifyVariable(method = "renderHandsWithItems", at = @At(value = "STORE", ordinal = 0), ordinal = 6)
    private float clockware$modifyHeightRightForDualwield(float height, @Local InteractionHand interactionHand, @Local(argsOnly = true) float partialTicks){
        if(this.minecraft.player==null)
            return height;

        if(Clockware_Util.hasDualwieldClockware(this.minecraft.player)){
            return 1.0F - Mth.lerp(partialTicks, this.oMainHandHeight, this.mainHandHeight);
        }

        return height;
    }

    @ModifyVariable(method = "renderHandsWithItems", at = @At(value = "STORE", ordinal = 1), ordinal = 6)
    private float clockware$modifyHeightLeftForDualwield(float height, @Local InteractionHand interactionHand, @Local(argsOnly = true) float partialTicks){
        if(this.minecraft.player==null)
            return height;

        if(Clockware_Util.hasDualwieldClockware(this.minecraft.player)){
            return 1.0F - Mth.lerp(partialTicks, this.oMainHandHeight, this.mainHandHeight);
        }

        return height;
    }

    @ModifyVariable(method = "renderArmWithItem", at = @At(value = "LOAD", ordinal = 1))
    private boolean clockware$showHandIfDualwield(boolean original, @Local(argsOnly = true) InteractionHand hand, @Local(argsOnly = true) AbstractClientPlayer player) {

        if(Clockware_Util.hasDualwieldClockware(player)){
            if(player.clockware$getClockwareOnOffHand().getItem() instanceof DualWieldClockware dualWieldClockware && dualWieldClockware.showOffHand()){
                return original || hand == InteractionHand.OFF_HAND;
            }
        }

        return original;
    }

    //Anim hand clockware
    @Inject(method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD"), cancellable = true
    )
    private void clockware$setClockwareAnim(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        boolean flag = hand == InteractionHand.MAIN_HAND;
        HumanoidArm humanoidarm = flag ? player.getMainArm() : player.getMainArm().getOpposite();

        if (player.clockware$isRightClicking() && !player.isInvisible() && !(player.getMainHandItem().getItem() instanceof CrossbowItem) && !player.isUsingItem()) {

            HumanoidArm mainArm = player.getMainArm();
            HumanoidArm offHandArm = player.getMainArm().getOpposite();

            ClockwareSlotType slotForMainHand =  mainArm.equals(HumanoidArm.RIGHT) ? ClockwareSlotType.RIGHT_ARM : ClockwareSlotType.LEFT_ARM;
            ClockwareSlotType slotForOffHand  =  offHandArm.equals(HumanoidArm.RIGHT) ? ClockwareSlotType.RIGHT_ARM : ClockwareSlotType.LEFT_ARM;

            ClockwareItem clockwareOnMain = player.clockware$getClockwareBySlot(slotForMainHand).getItem() instanceof ClockwareItem c1 && c1.getUseAnimation()!= UseAnim.NONE? c1: null;
            ClockwareItem clockwareOnOff =  player.clockware$getClockwareBySlot(slotForOffHand).getItem()  instanceof ClockwareItem c2 && c2.getUseAnimation()!= UseAnim.NONE? c2: null;

            int k = humanoidarm == HumanoidArm.RIGHT ? 1 : -1;

            if(humanoidarm.equals(mainArm)){
                //Set transformations on main
                if(clockwareOnMain != null && player.getMainHandItem().isEmpty()){

                    poseStack.pushPose();
                    c$setTransformations(clockwareOnMain, player, partialTicks, humanoidarm, swingProgress, clockwareOnMain.getDefaultInstance(), equippedProgress, poseStack, k);
                    this.renderPlayerArm(poseStack, buffer, combinedLight, equippedProgress, swingProgress, humanoidarm);
                    poseStack.popPose();
                    ci.cancel();
                }
            } else if(humanoidarm.equals(offHandArm)){
                //Set transformations on off
                if(clockwareOnOff != null && clockwareOnOff.isUsableOnOffHand() && player.getOffhandItem().isEmpty()){

                    poseStack.pushPose();
                    c$setTransformations(clockwareOnOff, player, partialTicks, humanoidarm, swingProgress, clockwareOnOff.getDefaultInstance(), equippedProgress, poseStack, k);
                    this.renderPlayerArm(poseStack, buffer, combinedLight, equippedProgress, swingProgress, humanoidarm);
                    poseStack.popPose();
                    ci.cancel();
                }
            }
        }
    }

    @Unique
    private void c$setTransformations(ClockwareItem clockware, AbstractClientPlayer player, float partialTicks, HumanoidArm humanoidarm, float swingProgress, ItemStack clockwareStack, float equippedProgress, PoseStack poseStack, int k){
        assert minecraft.player != null;
        boolean custom = IClientItemExtensions.of(clockwareStack).applyForgeHandTransform(poseStack, minecraft.player, humanoidarm, clockwareStack, partialTicks, equippedProgress, swingProgress);
        if (!custom) // FORGE: Allow items to define custom arm animation
            switch (clockware.getUseAnimation()){
            case NONE:
                return;
            case DRINK:
                this.applyEatTransform(poseStack, partialTicks, humanoidarm, clockwareStack, player);
                this.applyItemArmTransform(poseStack, humanoidarm, equippedProgress);
                return;
            case BLOCK:
                this.applyItemArmTransform(poseStack, humanoidarm, equippedProgress);
                return;
            case BOW:
                this.applyItemArmTransform(poseStack, humanoidarm, equippedProgress);
                poseStack.translate((float)k * -0.2785682F, 0.18344387F, 0.15731531F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-13.935F));
                poseStack.mulPose(Axis.YP.rotationDegrees((float)k * 35.3F));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float)k * -9.785F));
                float f8 = (float)clockwareStack.getUseDuration(player) - ((float)player.getUseItemRemainingTicks() - partialTicks + 1.0F);
                float f12 = f8 / 20.0F;
                f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                if (f12 > 1.0F) {
                    f12 = 1.0F;
                }

                if (f12 > 0.1F) {
                    float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                    float f18 = f12 - 0.1F;
                    float f20 = f15 * f18;
                    poseStack.translate(f20 * 0.0F, f20 * 0.004F, f20 * 0.0F);
                }

                poseStack.translate(f12 * 0.0F, f12 * 0.0F, f12 * 0.04F);
                poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                poseStack.mulPose(Axis.YN.rotationDegrees((float)k * 45.0F));
                return;
            case SPEAR:
                this.applyItemArmTransform(poseStack, humanoidarm, equippedProgress);
                poseStack.translate((float)k * -0.5F, 0.7F, 0.1F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-55.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees((float)k * 35.3F));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float)k * -9.785F));
                float f7 = (float)clockwareStack.getUseDuration(player) - ((float)player.getUseItemRemainingTicks() - partialTicks + 1.0F);
                float f11 = f7 / 10.0F;
                if (f11 > 1.0F) {
                    f11 = 1.0F;
                }

                if (f11 > 0.1F) {
                    float f14 = Mth.sin((f7 - 0.1F) * 1.3F);
                    float f17 = f11 - 0.1F;
                    float f19 = f14 * f17;
                    poseStack.translate(f19 * 0.0F, f19 * 0.004F, f19 * 0.0F);
                }

                poseStack.translate(0.0F, 0.0F, f11 * 0.2F);
                poseStack.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                poseStack.mulPose(Axis.YN.rotationDegrees((float)k * 45.0F));
                return;
            case BRUSH:
                this.applyBrushTransform(poseStack, partialTicks, humanoidarm, clockwareStack, player, equippedProgress);
            }
    }
}
