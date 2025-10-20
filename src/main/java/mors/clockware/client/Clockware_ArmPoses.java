package mors.clockware.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

@OnlyIn(Dist.CLIENT)
public class Clockware_ArmPoses  {

    public static final EnumProxy<HumanoidModel.ArmPose> PROJECTILE_LAUNCHER = new EnumProxy<>(HumanoidModel.ArmPose.class, false, (IArmPoseTransformer)Clockware_ArmPoses::projectileLauncher);

    @SuppressWarnings("all")
    private static void projectileLauncher(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        if(arm.equals(HumanoidArm.RIGHT)){

            model.rightArm.yRot =  -model.head.xRot;
            model.rightArm.xRot =   model.rightArm.yRot > 1.2f?  -1.5f - (entity.isCrouching() ? (float) (Math.PI / 12) : 0.0F) : (model.head.yRot + Mth.DEG_TO_RAD * -90) - (entity.isCrouching() ? (float) (Math.PI / 12) : 0.0F);


            model.rightArm.zRot +=  Mth.DEG_TO_RAD * 90;
            model.rightArm.x     = model.rightArm.x - 1;
        } else {

            model.leftArm.yRot =   model.head.xRot ;
            model.leftArm.xRot =   model.leftArm.yRot < -1.2f?  -1.5f - (entity.isCrouching() ? (float) (Math.PI / 12) : 0.0F) : (-model.head.yRot + Mth.DEG_TO_RAD * -90) - (entity.isCrouching() ? (float) (Math.PI / 12) : 0.0F);

            model.leftArm.zRot -=  Mth.DEG_TO_RAD * 90;
            model.leftArm.x     = model.leftArm.x + 1;
        }
    }
}
