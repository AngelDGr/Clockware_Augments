package mors.clockware.mixin.clockware;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.registry.Clockware_Advancements;
import mors.clockware.registry.Clockware_Items;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

public class GolemArmMixins {

    @Mixin(HandCrankBlock.class)
    public static class HandCrankBlockMixin {

        @Unique
        private int c$golemArmSpeedIncrease =-1;

        @ModifyArg(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
        private float clockware$lessHungerHandCrank(float exhaustion, @Local(argsOnly = true) Player player) {

            if(clockware$onMainArm(player, Clockware_Items.GOLEM_ARM_PROTOTYPE.get())){
                c$golemArmSpeedIncrease =2;
                return exhaustion*0.5f;
            } else if(clockware$onMainArm(player, Clockware_Items.GOLEM_ARM_REFINED.get())){
                c$golemArmSpeedIncrease =3;
                return exhaustion*0.25f;
            } else if (clockware$onMainArm(player, Clockware_Items.GOLEM_ARM_MASTERWORK.get())) {
                if(player instanceof ServerPlayer serverPlayer)
                    Clockware_Advancements.GOLEM_HAND_CRANK.get().trigger(serverPlayer);
                c$golemArmSpeedIncrease =4;
                return exhaustion*0.05f;
            }

            c$golemArmSpeedIncrease =-1;
            return exhaustion;
        }

        @ModifyReturnValue(method = "getRotationSpeed", at = @At("RETURN"))
        private int clockware$increaseSpeed(int original){
            if(c$golemArmSpeedIncrease>0){
                return original*c$golemArmSpeedIncrease;
            }
            return original;
        }

        @Unique
        public boolean clockware$onMainArm(Player player, Item item) {
            if(player.getMainArm().equals(HumanoidArm.RIGHT))
                return clockware$isClockwareOnSlotSameAs(player, ClockwareSlotType.RIGHT_ARM, item);

            if(player.getMainArm().equals(HumanoidArm.LEFT))
                return clockware$isClockwareOnSlotSameAs(player, ClockwareSlotType.LEFT_ARM, item);

            return clockware$isClockwareOnSlotSameAs(player, ClockwareSlotType.RIGHT_ARM, item);
        }

        @Unique
        public boolean clockware$isClockwareOnSlotSameAs(Player player, ClockwareSlotType slot, Item item) {
            return ItemStack.isSameItem(player.clockware$getClockwareInventory().get(slot.getIndex()), item.getDefaultInstance());
        }

    }
}
