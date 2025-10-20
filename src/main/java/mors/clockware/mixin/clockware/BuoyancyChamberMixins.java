package mors.clockware.mixin.clockware;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

public class BuoyancyChamberMixins {

    @Mixin(LivingEntity.class)
    public abstract static class LivingEntityMixin extends Entity implements Attackable, ILivingEntityExtension {

        public LivingEntityMixin(EntityType<?> entityType, Level level) { super(entityType, level);}

        @Unique
        public final LivingEntity c$this = (LivingEntity) (Object) this;

        @ModifyVariable(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"), ordinal = 0)
        private double clockware$reducesGravity(double constant) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "buoyancy_chamber", c$this.clockware$getClockwareInventory())){
                boolean flag = this.getDeltaMovement().y <= 0.0;
                ItemStack clockwareOnBody= c$this.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla    -> 0.08
                //Prototype  -> 0.06
                //Refined    -> 0.04
                //Masterwork -> 0.03
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem && flag && !c$this.isFallFlying())
                    return Math.min(constant, Math.max(this.getGravity() - ((clockwareItem.getLevel()+1)*0.02), 0.03));
            }

            return constant;
        }

        @ModifyReturnValue(method = "calculateFallDamage", at = @At(value = "RETURN"))
        private int clockware$reducesFallDamage(int damage) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "buoyancy_chamber", c$this.clockware$getClockwareInventory())){
                ItemStack clockwareOnBody= c$this.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Prototype  -> -30%
                //Refined    -> -60%
                //Masterwork -> -90%
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    return (int) (damage - (damage * ((clockwareItem.getLevel()+1) * 0.30)));
            }

            return damage;
        }

        @ModifyVariable(method = "getFluidFallingAdjustedMovement", at = @At(value = "STORE", ordinal = 0), ordinal = 1)
        private double clockware$reducesFallFluidSpeed1(double d0) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "buoyancy_chamber", c$this.clockware$getClockwareInventory())
                    && !c$this.isShiftKeyDown()){

                ItemStack clockwareOnBody= c$this.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Prototype  -> 0.002
                //Refined    -> 0.004
                //Masterwork -> 0.006
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    return clockwareItem.getLevel()==0? -0.002 : clockwareItem.getLevel()==1? -0.001: 0;
            }

            return d0;
        }

        @ModifyVariable(method = "getFluidFallingAdjustedMovement", at = @At(value = "STORE", ordinal = 1), ordinal = 1)
        private double clockware$reducesFallFluidSpeed2(double d0, @Local(argsOnly = true) double gravity, @Local(argsOnly = true) Vec3 deltaMovement) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "buoyancy_chamber", c$this.clockware$getClockwareInventory())
                    && !c$this.isShiftKeyDown()){

                ItemStack clockwareOnBody= c$this.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Prototype  -> 0.06
                //Refined    -> 0.04
                //Masterwork -> 0.02
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    return deltaMovement.y - (gravity-(0.02*clockwareItem.getLevel())) / 16.0;
            }

            return d0;
        }

        @ModifyVariable(method = "travel", at = @At(value = "LOAD", ordinal = 8), index = 6)
        private Vec3 clockware$increaseElytraSpeed(Vec3 elytraVec3) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "buoyancy_chamber", c$this.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= c$this.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla    -> 30b/s
                //Prototype  -> 36b/s
                //Refined    -> 42b/s
                //Masterwork -> 48b/s
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    if (c$this.isFallFlying()) {
                        Vec3 look = c$this.getLookAngle();
                        double acceleration = 0.003 + (clockwareItem.getLevel()* 0.003);

                        return elytraVec3.add(look.x * acceleration, look.y * acceleration * 0.5, look.z * acceleration);
                    }
            }

            return elytraVec3;
        }
    }
}
