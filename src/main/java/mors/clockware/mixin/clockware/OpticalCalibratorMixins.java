package mors.clockware.mixin.clockware;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.registry.Clockware_Tags;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

public class OpticalCalibratorMixins {

    @Mixin(Projectile.class)
    public static class ProjectileMixin {
        @ModifyVariable(method = "shootFromRotation", at = @At(value = "LOAD"), ordinal = 4, argsOnly = true)
        private float clockware$removeInaccuracy(float inaccuracy, @Local(argsOnly = true) Entity shooter){

            if(shooter instanceof LivingEntity entityLiving)
                if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "optical_calibrator", entityLiving.clockware$getClockwareInventory()))
                    return 0;


            return inaccuracy;
        }
    }

    @Mixin(ProjectileWeaponItem.class)
    public static class ProjectileWeaponMixin {

        @ModifyArg(method = "shoot", at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ProjectileWeaponItem;shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V"),
                index = 4)
        private float clockware$removeInaccuracy(float inaccuracy, @Local(argsOnly = true, index = 2) LivingEntity shooter){

            if(shooter instanceof LivingEntity entityLiving)
                if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "optical_calibrator", entityLiving.clockware$getClockwareInventory()))
                    return 0;

            return inaccuracy;
        }
    }

    @Mixin(LivingEntity.class)
    public abstract static class LivingEntityMixin {

        @Unique
        public final LivingEntity c$this = (LivingEntity) (Object) this;

        @ModifyReturnValue(method = "canBeAffected", at = @At("RETURN"))
        private boolean clockware$visualEffectImmunity(boolean original, @Local(argsOnly = true) MobEffectInstance effectInstance){

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "optical_calibrator", c$this.clockware$getClockwareInventory())){
                ItemStack clockwareOnHead= c$this.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

                if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>0){
                    return original && !(effectInstance.getEffect().is(Clockware_Tags.OPTICAL_CALIBRATOR_EFFECT_IMMUNITY));
                }
            }

            return original;
        }
    }

}
