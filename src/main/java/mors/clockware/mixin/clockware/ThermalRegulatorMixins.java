package mors.clockware.mixin.clockware;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.MagmaBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ThermalRegulatorMixins {

    @Mixin(Entity.class)
    public static class EntityMixin {

        @Unique
        public final Entity c$this = (Entity) (Object) this;

        @ModifyConstant(method = "baseTick", constant = @Constant(intValue = 20))
        private int clockware$reducesFireDamageTicks(int constant) {
            if(c$this instanceof LivingEntity livingEntity
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", livingEntity.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= livingEntity.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla   -> 20
                //Prototype -> 40
                //Refined   -> 60
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    return constant + ((clockwareItem.getLevel()+1) * 20);
            }

            return constant;
        }

        @ModifyReturnValue(method = "getTicksRequiredToFreeze", at = @At("RETURN"))
        private int clockware$reducesToFreezeTicks(int constant) {
            if(c$this instanceof LivingEntity livingEntity
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", livingEntity.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= livingEntity.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla   -> 140
                //Prototype -> 180
                //Refined   -> 210
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    return constant + ((clockwareItem.getLevel()+1) * 40);
            }

            return constant;
        }

        @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;fireImmune()Z"))
        private boolean clockware$removesFlamesFaster(boolean original) {

            if(c$this instanceof LivingEntity livingEntity
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", livingEntity.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= livingEntity.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel() >= 2){
                    return true;
                }
            }

            return original;
        }

        @ModifyArg(method = "lavaHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
        private float clockware$reduceLavaDamage(float damage) {

            if(c$this instanceof LivingEntity livingEntity
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", livingEntity.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= livingEntity.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel() > 0){
                    return damage / (2 * clockwareItem.getLevel());
                }
            }

            return damage;
        }

        @WrapWithCondition(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
        private boolean clockware$negatesFireDamage(Entity entity, DamageSource source, float amount) {

            if(entity instanceof LivingEntity livingEntity
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", livingEntity.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= livingEntity.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Masterwork -> NEVER
                return !(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem) || clockwareItem.getLevel() < 2;
            }

            return true;
        }
    }

    @Mixin(LivingEntity.class)
    public static class LivingEntityMixin {

        @Unique
        public final LivingEntity c$this = (LivingEntity) (Object) this;

        @ModifyReturnValue(method = "canFreeze", at = @At("RETURN"))
        private boolean clockware$negatesFreezing(boolean original) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", c$this.clockware$getClockwareInventory())){
                ItemStack clockwareOnBody= c$this.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Only at masterwork level
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>1)
                    return false;
            }

            return original;
        }

        @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
        private void clockware$negatesFreezingAndBurningDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", c$this.clockware$getClockwareInventory())){
                ItemStack clockwareOnBody= c$this.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Only at masterwork level
                if((source.is(DamageTypeTags.IS_FREEZING) || (source.is(DamageTypeTags.IS_FIRE) && !source.is(DamageTypes.LAVA)))
                        && clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>=2)
                    cir.setReturnValue(false);
            }
        }

        @ModifyConstant(method = "aiStep", constant = @Constant(intValue = 40))
        private int clockware$reducesFreezeDamageTicks(int constant) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", c$this.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= c$this.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla   -> 40
                //Prototype -> 60
                //Refined   -> 80
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    return constant + ((clockwareItem.getLevel()+1) * 20);
            }

            return constant;
        }
    }

    @Mixin(BaseFireBlock.class)
    public static class BaseFireBlockMixin {

        @WrapWithCondition(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
        private boolean clockware$reducesInsideFireDamage(Entity entity, DamageSource source, float amount) {

            if(entity instanceof LivingEntity livingEntity
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", livingEntity.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= livingEntity.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla   -> 0
                //Prototype -> 20
                //Refined   -> 40
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()<2){
                    return entity.tickCount % ((clockwareItem.getLevel()+1) * 20)==0;
                }


                //Masterwork -> NEVER
                return !(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem) || clockwareItem.getLevel() < 2;

            }

            return true;
        }
    }

    @Mixin(MagmaBlock.class)
    public static class MagmaBlockMixin {
        @WrapWithCondition(method = "stepOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
        private boolean clockware$reducesOverMagmaDamage(Entity entity, DamageSource source, float amount) {

            if(entity instanceof LivingEntity livingEntity
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", livingEntity.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= livingEntity.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla   -> 0
                //Prototype -> 10
                //Refined   -> 20
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()<2){
                    return entity.tickCount % ((clockwareItem.getLevel()+1) * 10)==0;
                }
            }

            return true;
        }
    }

    @Mixin(CampfireBlock.class)
    public static class CampfireBlockMixin {
        @WrapWithCondition(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
        private boolean clockware$reducesInsideCampfireDamage(Entity entity, DamageSource source, float amount) {

            if(entity instanceof LivingEntity livingEntity
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "thermal_regulator", livingEntity.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= livingEntity.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla   -> 0
                //Prototype -> 20
                //Refined   -> 40
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()<2){
                    return entity.tickCount % ((clockwareItem.getLevel()+1) * 20)==0;
                }
            }

            return true;
        }
    }
}
