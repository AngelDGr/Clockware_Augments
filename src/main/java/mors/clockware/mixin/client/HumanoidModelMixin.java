package mors.clockware.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This handle the animation on third person for dual-wielding
 */
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {

    @Shadow
    protected abstract HumanoidArm getAttackArm(T entity);

    @Shadow protected abstract ModelPart getArm(HumanoidArm side);

    @Shadow @Final
    public ModelPart body;

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart head;

    @WrapWithCondition(method = "setupAttackAnimation",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/geom/ModelPart;z:F",  opcode = Opcodes.PUTFIELD))
    private boolean clockware$avoidModifyArmZ(ModelPart instance, float z, @Local(argsOnly = true) T livingEntity){
        return !(Clockware_Util.hasDualwieldClockware(livingEntity));
    }

    @WrapWithCondition(method = "setupAttackAnimation",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/geom/ModelPart;x:F",  opcode = Opcodes.PUTFIELD))
    private boolean clockware$avoidModifyArmX(ModelPart instance, float x, @Local(argsOnly = true) T livingEntity){
        return !(Clockware_Util.hasDualwieldClockware(livingEntity));
    }

    @WrapWithCondition(method = "setupAttackAnimation",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/geom/ModelPart;yRot:F",  opcode = Opcodes.PUTFIELD))
    private boolean clockware$avoidModifyArmYRot(ModelPart instance, float yRot, @Local(argsOnly = true) T livingEntity){
        return !(Clockware_Util.hasDualwieldClockware(livingEntity));
    }

    @WrapWithCondition(method = "setupAttackAnimation",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/geom/ModelPart;zRot:F",  opcode = Opcodes.PUTFIELD))
    private boolean clockware$avoidModifyArmZRot(ModelPart instance, float zRot, @Local(argsOnly = true) T livingEntity){
        return !(Clockware_Util.hasDualwieldClockware(livingEntity));
    }

    @WrapWithCondition(method = "setupAttackAnimation",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/geom/ModelPart;xRot:F",  opcode = Opcodes.PUTFIELD))
    private boolean clockware$avoidModifyArmXRot(ModelPart instance, float xRot, @Local(argsOnly = true) T livingEntity){
        return !(Clockware_Util.hasDualwieldClockware(livingEntity));
    }

    @Inject(method = "setupAttackAnimation", at = @At("TAIL"))
    private void clockware$animateHandsDualwield(T livingEntity, float ageInTicks, CallbackInfo ci){
        if(Clockware_Util.hasDualwieldClockware(livingEntity)){


            if (!(this.attackTime <= 0.0F)) {
                this.rightArm.xRot = 0.0F;
                this.leftArm.xRot = 0.0F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
                this.rightArm.zRot = 0.0F;
                this.leftArm.zRot = 0.0F;

                //MainHand
                {
                    HumanoidArm humanoidArm = this.getAttackArm(livingEntity);
                    ModelPart modelPart = this.getArm(humanoidArm);
                    float f = this.attackTime;
                    this.body.yRot = Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2)) * 0.2F;
                    if (humanoidArm == HumanoidArm.LEFT) {
                        this.body.yRot *= -1.0F;
                    }

                    this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
                    this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;

                    this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
                    this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;

                    this.rightArm.yRot = this.rightArm.yRot + this.body.yRot;
                    this.leftArm.yRot = this.leftArm.yRot + this.body.yRot;
                    this.leftArm.xRot = this.leftArm.xRot + this.body.yRot;


                    f = 1.0F - this.attackTime;
                    f *= f;
                    f *= f;
                    f = 1.0F - f;
                    float g = Mth.sin(f * (float) Math.PI);
                    float h = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
                    modelPart.xRot -= g * 1.2F + h;
                    modelPart.yRot = modelPart.yRot + this.body.yRot * 2.0F;
                    modelPart.zRot = modelPart.zRot + Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
                }

                //OffHand
                {
                    HumanoidArm oppositeArm = this.getAttackArm(livingEntity).getOpposite();
                    ModelPart modelPart = this.getArm(oppositeArm);
                    float f;

                    int changeDirection= -1;

                    f = 1.0F - this.attackTime;
                    f *= f;
                    f *= f;
                    f = 1.0F - f;
                    float g = Mth.sin(f * (float) Math.PI);
                    float h = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;

                    modelPart.xRot -= (g * 1.2F + h);
                    modelPart.yRot = (modelPart.yRot + this.body.yRot * 2.0F) * changeDirection;
                    modelPart.zRot = (modelPart.zRot + Mth.sin(this.attackTime * (float) Math.PI) * -0.4F)*changeDirection;
                }
            }
        }
    }
}
