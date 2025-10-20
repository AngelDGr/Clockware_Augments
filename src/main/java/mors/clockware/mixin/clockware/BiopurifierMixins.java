package mors.clockware.mixin.clockware;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

public class BiopurifierMixins {

    @Mixin(LivingEntity.class)
    public abstract static class LivingEntityMixin {

        @Unique
        public final LivingEntity c$this = (LivingEntity) (Object) this;

        @WrapWithCondition(method = "addEatEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
        private boolean clockware$preventHarmfulEffectsWhenEat(LivingEntity instance, MobEffectInstance effectInstance){

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "biopurifier", c$this.clockware$getClockwareInventory())){
                return !effectInstance.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL);
            }

            return true;
        }
    }

    @Mixin(PotionItem.class)
    public abstract static class PotionItemMixin {


        @WrapWithCondition(method = "lambda$finishUsingItem$0", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
        private static boolean clockware$preventHarmfulEffectsWhenDrink(LivingEntity instance, MobEffectInstance effectInstance, @Local(argsOnly = true) LivingEntity entityLiving){

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "biopurifier", entityLiving.clockware$getClockwareInventory())){

                ItemStack clockwareOnHead= entityLiving.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

                if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>0)
                    return !effectInstance.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL);
            }

            return true;
        }

        @WrapWithCondition(method = "lambda$finishUsingItem$0", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/effect/MobEffect;applyInstantenousEffect(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/LivingEntity;ID)V"))
        private static boolean clockware$preventHarmfulEffectsWhenDrinkInstant(MobEffect instance, Entity source, Entity indirectSource, LivingEntity livingEntity, int amplifier, double health, @Local(argsOnly = true) LivingEntity entityLiving){

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "biopurifier", entityLiving.clockware$getClockwareInventory())){

                ItemStack clockwareOnHead= entityLiving.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

                if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>0)
                    return !instance.getCategory().equals(MobEffectCategory.HARMFUL);
            }

            return true;
        }
    }

    @Mixin(ThrownPotion.class)
    public abstract static class ThrownPotionMixin {

        @WrapWithCondition(method = "applySplash", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
        private static boolean clockware$preventHarmfulEffectsWhenSplash(LivingEntity entityLiving, MobEffectInstance effectInstance, Entity entity){

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "biopurifier", entityLiving.clockware$getClockwareInventory())){

                ItemStack clockwareOnHead= entityLiving.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

                if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>0)
                    return !effectInstance.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL);
            }

            return true;
        }

        @WrapWithCondition(method = "applySplash", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/effect/MobEffect;applyInstantenousEffect(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/LivingEntity;ID)V"))
        private static boolean clockware$preventHarmfulEffectsWhenSplashInstant(MobEffect instance, Entity source, Entity indirectSource, LivingEntity entityLiving, int amplifier, double health){


            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "biopurifier", entityLiving.clockware$getClockwareInventory())){

                ItemStack clockwareOnHead= entityLiving.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

                if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>0)
                    return !instance.getCategory().equals(MobEffectCategory.HARMFUL);
            }

            return true;
        }
    }

    @Mixin(AreaEffectCloud.class)
    public abstract static class AreaEffectCloudMixin {

        @WrapWithCondition(method = "tick", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
        private static boolean clockware$preventHarmfulEffectsCloud(LivingEntity entityLiving, MobEffectInstance effectInstance, Entity entity){
            
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "biopurifier", entityLiving.clockware$getClockwareInventory())){

                ItemStack clockwareOnHead= entityLiving.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

                if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>1)
                    return !effectInstance.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL);
            }

            return true;
        }

        @WrapWithCondition(method = "tick", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/effect/MobEffect;applyInstantenousEffect(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/LivingEntity;ID)V"))
        private static boolean clockware$preventHarmfulEffectsCloudInstant(MobEffect instance, Entity source, Entity indirectSource, LivingEntity entityLiving, int amplifier, double health){

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "biopurifier", entityLiving.clockware$getClockwareInventory())){

                ItemStack clockwareOnHead= entityLiving.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

                if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getLevel()>1)
                    return !instance.getCategory().equals(MobEffectCategory.HARMFUL);
            }

            return true;
        }
    }
}
