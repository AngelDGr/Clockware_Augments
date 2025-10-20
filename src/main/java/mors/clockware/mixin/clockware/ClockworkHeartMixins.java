package mors.clockware.mixin.clockware;

import com.llamalad7.mixinextras.sugar.Local;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

public class ClockworkHeartMixins {

    @Mixin(FoodData.class)
    public static class FoodDataMixin {

        @ModifyConstant(method = "tick", constant = @Constant(intValue = 80, ordinal = 0))
        private int clockware$increasesRegenSpeed(int constant, @Local(argsOnly = true) Player player) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "clockwork_heart", player.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= player.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla    -> 80
                //Prototype  -> 60
                //Refined    -> 40
                //Masterwork -> 20
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    return Math.max(constant - ((clockwareItem.getLevel()+1)*20), 1);
            }

            return constant;
        }

        @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"))
        private float clockware$reduceExhaustionFromRegen(float exhaustion, @Local(argsOnly = true) Player player) {
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, "clockwork_heart", player.clockware$getClockwareInventory())){

                ItemStack clockwareOnBody= player.clockware$getClockwareBySlot(ClockwareSlotType.BODY);

                //Vanilla    -> 6
                //Prototype  -> 5
                //Refined    -> 4
                //Masterwork -> 3
                if(clockwareOnBody.getItem() instanceof ClockwareItem clockwareItem)
                    return Math.max(exhaustion - ((clockwareItem.getLevel() + 1)), 1);
            }

            return exhaustion;
        }
    }
}
