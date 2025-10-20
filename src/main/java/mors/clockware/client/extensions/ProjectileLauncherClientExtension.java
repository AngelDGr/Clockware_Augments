package mors.clockware.client.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mors.clockware.client.Clockware_ArmPoses;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.item.ProjectileLauncherArm;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProjectileLauncherClientExtension implements IClientItemExtensions {

    @Override
    public boolean applyForgeHandTransform(
            @NotNull PoseStack poseStack,
            @NotNull LocalPlayer player,
            @NotNull HumanoidArm arm,
            @NotNull ItemStack stack,
            float partialTick,
            float equipProcess,
            float swingProcess) {

        //This is to ensure that neoforge doesn't animate when just holding the clockware item on the hand
        if(player.getItemInHand(player.getMainArm()== arm? InteractionHand.MAIN_HAND: InteractionHand.OFF_HAND).isEmpty() && player.clockware$isRightClicking()){
            ItemStack clockware= player.clockware$getClockwareBySlot(arm.equals(HumanoidArm.RIGHT)? ClockwareSlotType.RIGHT_ARM: ClockwareSlotType.LEFT_ARM);

            if(clockware.getItem() instanceof ProjectileLauncherArm projectileLauncherArm){
                int clockwareLevel = projectileLauncherArm.getLevel();

                //Different durations
                float maxDuration = clockwareLevel==0? 40: clockwareLevel==1? 30: 20;

                float scaleRate = player.clockware$getRightUseTime() / maxDuration;
                if (scaleRate > 1.0F) {
                    scaleRate = 1.0F;
                }

                // Determine if it's the right or left arm
                boolean isRight = arm == HumanoidArm.RIGHT;

                poseStack.translate(
                        isRight? -0.3 : 0.3,
                        -0.25,
                        -1.0
                );


                poseStack.mulPose(Axis.XP.rotationDegrees(-120.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(isRight? 18F: -18f));

                //Charge animation
                poseStack.scale(1.0F, 1.0F, 1.0F + (scaleRate * 0.2F));

                return true;
            }
        }

        return false;
    }

    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(@NotNull LivingEntity entityLiving, @NotNull InteractionHand hand, @NotNull ItemStack itemStack) {
        //This is to ensure that neoforge doesn't animate when just holding the clockware item on the hand
        if(entityLiving.getItemInHand(hand).isEmpty())
            return Clockware_ArmPoses.PROJECTILE_LAUNCHER.getValue();

        return null;
    }
}
