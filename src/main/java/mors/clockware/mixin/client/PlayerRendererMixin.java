package mors.clockware.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mors.clockware.client.layer.*;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Client_Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin<T extends LivingEntity>  extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PlayerRendererMixin(final EntityRendererProvider.Context ctx, final PlayerModel<AbstractClientPlayer> model, final float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "renderHand", at = @At("TAIL"))
    private void clockware$renderClockwareHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci) {
        var layers= this.layers;
        if(rendererArm==this.model.rightArm){

            var clockwareLayer = layers.stream()
                    .filter(layer -> layer instanceof ClockwareRightArmLayer<?, ?>)
                    .map(layer -> (ClockwareRightArmLayer<T, ?>) layer)
                    .findFirst();

            clockwareLayer.ifPresent(
                    layer ->
                            layer.render(poseStack, buffer, combinedLight, (T) player,
                                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        } else if(rendererArm==this.model.leftArm){
            var clockwareLayer = layers.stream()
                    .filter(layer -> layer instanceof ClockwareLeftArmLayer<?, ?>)
                    .map(layer -> (ClockwareLeftArmLayer<T, ?>) layer)
                    .findFirst();

            clockwareLayer.ifPresent(
                    layer ->
                            layer.render(poseStack, buffer, combinedLight, (T) player,
                                    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        }
    }



    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void clockware$setArmPoseClockware(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        if (player.clockware$isRightClicking() && !player.isUsingItem()) {
            HumanoidArm mainArm = player.getMainArm();
            HumanoidArm offHandArm = player.getMainArm().getOpposite();

            ClockwareSlotType slotForMainHand =  mainArm.equals(HumanoidArm.RIGHT) ? ClockwareSlotType.RIGHT_ARM : ClockwareSlotType.LEFT_ARM;
            ClockwareSlotType slotForOffHand  =  offHandArm.equals(HumanoidArm.RIGHT) ? ClockwareSlotType.RIGHT_ARM : ClockwareSlotType.LEFT_ARM;

            ClockwareItem clockwareOnMain = player.clockware$getClockwareBySlot(slotForMainHand).getItem() instanceof ClockwareItem c1
                    && (Client_Util.getArmPoseFromString(c1.getArmPose())!=HumanoidModel.ArmPose.EMPTY
                    || IClientItemExtensions.of(c1.getDefaultInstance()).getArmPose(player, hand, c1.getDefaultInstance())!=null)?
                    c1: null;
            ClockwareItem clockwareOnOff =  player.clockware$getClockwareBySlot(slotForOffHand).getItem() instanceof ClockwareItem c2
                    && (Client_Util.getArmPoseFromString(c2.getArmPose())!=HumanoidModel.ArmPose.EMPTY
                    || IClientItemExtensions.of(c2.getDefaultInstance()).getArmPose(player, hand, c2.getDefaultInstance())!=null)?
                    c2: null;

            //Set pose on main
            if(clockwareOnMain!=null
                    && hand==InteractionHand.MAIN_HAND
                    && player.getMainHandItem().isEmpty()){

                HumanoidModel.ArmPose forgeArmPose = IClientItemExtensions.of(clockwareOnMain.getDefaultInstance()).getArmPose(player, hand, clockwareOnMain.getDefaultInstance());

                if(forgeArmPose!=null) cir.setReturnValue(forgeArmPose);
                else cir.setReturnValue(Client_Util.getArmPoseFromString(clockwareOnMain.getArmPose()));
            }

            //Set anim on off
            if(clockwareOnOff!=null
                    && hand==InteractionHand.OFF_HAND
                    //Only if it is usable
                    && clockwareOnOff.isUsableOnOffHand()
                    && player.getOffhandItem().isEmpty()){

                HumanoidModel.ArmPose forgeArmPose = IClientItemExtensions.of(clockwareOnOff.getDefaultInstance()).getArmPose(player, hand, clockwareOnOff.getDefaultInstance());

                if(forgeArmPose!=null) cir.setReturnValue(forgeArmPose);
                else cir.setReturnValue(Client_Util.getArmPoseFromString(clockwareOnOff.getArmPose()));
            }
        }
    }
}
