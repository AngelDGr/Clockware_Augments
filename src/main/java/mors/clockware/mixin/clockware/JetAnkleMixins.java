package mors.clockware.mixin.clockware;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

public class JetAnkleMixins {

    @Mixin(LivingEntity.class)
    public static class LivingEntityMixin {

        @Unique
        LivingEntity c$this = (LivingEntity)(Object) this;

        //Vanilla    -> 1
        //Prototype  -> 1.5
        //Refined    -> 2.5
        //Masterwork -> 3.0
        @ModifyReturnValue(method = "getJumpBoostPower", at = @At(value = "RETURN"))
        private float clockware$addJumpAnkle(float original){
            float clockwareAdditionRight = 0.0f;
            float clockwareAdditionLeft = 0.0f;

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_LEG, "jet_ankle", c$this.clockware$getClockwareInventory())){
                ItemStack clockwareStack = c$this.clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_LEG);

                if(clockwareStack.getItem() instanceof ClockwareItem clockwareItem){
                    clockwareAdditionRight=  0.1f * ((clockwareItem.getLevel()+1)*0.5f);
                }

            }

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_LEG, "jet_ankle", c$this.clockware$getClockwareInventory())){
                ItemStack clockwareStack = c$this.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_LEG);

                if(clockwareStack.getItem() instanceof ClockwareItem clockwareItem){
                    clockwareAdditionLeft= 0.1f * ((clockwareItem.getLevel()+1)*0.5f);
                }
            }

            return original + (clockwareAdditionLeft + clockwareAdditionRight);
        }
    }
}
