package mors.clockware.mixin.clockware;

import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HydrothrusterMixins {

    @Mixin(LivingEntity.class)
    public static class LivingEntityMixin {

        @Unique
        LivingEntity c$this = (LivingEntity)(Object) this;

        //Vanilla       -> 3.92b/s
        //Prototype     -> 4.48b/s -> 0.9 + 0.012 = 0.912
        //Refined       -> 5.20b/s -> 0.9 + 0.024 = 0.924
        //Masterwork    -> 6.10b/s -> 0.9 + 0.036 = 0.936
        //Dolphin Grace -> 9.80b/s -> 0.96 + 0.00 = 0.96
        @ModifyConstant(method = "travel", constant = @Constant(floatValue = 0.9f, ordinal = 0))
        private float clockware$addWaterSpeed(float original){
            float clockwareAdditionRight = 0.0f;
            float clockwareAdditionLeft = 0.0f;

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_LEG, "hydrothruster", c$this.clockware$getClockwareInventory())){
                ItemStack clockwareStack = c$this.clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_LEG);

                if(clockwareStack.getItem() instanceof ClockwareItem clockwareItem){
                    clockwareAdditionRight=  0.01f + (clockwareItem.getLevel()*0.01f);
                }
            }

            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_LEG, "hydrothruster", c$this.clockware$getClockwareInventory())){
                ItemStack clockwareStack = c$this.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_LEG);

                if(clockwareStack.getItem() instanceof ClockwareItem clockwareItem){
                    clockwareAdditionLeft= 0.01f + (clockwareItem.getLevel()*0.01f);
                }
            }

            float addition = (clockwareAdditionLeft + clockwareAdditionRight);
            //Caps at 99, just in case
            return Math.min(0.9f + (addition * 0.6f), 0.99f);
        }

        @Inject(method = "tick", at = @At("TAIL"))
        private void clockware$addBubbles(CallbackInfo ci) {
            if (!(c$this instanceof Player player)) return;
            if (!player.isInWater()) return;

            boolean hasRightThruster = Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_LEG, "hydrothruster", player.clockware$getClockwareInventory());
            boolean hasLeftThruster  = Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_LEG,  "hydrothruster", player.clockware$getClockwareInventory());

            if (!hasRightThruster && !hasLeftThruster) return;

            Level level = player.level();
            if (!(level instanceof ServerLevel serverLevel)) return;

            double baseY = player.getY() + 0.2;
            double forwardX = -Math.sin(Math.toRadians(player.getYRot()));
            double forwardZ =  Math.cos(Math.toRadians(player.getYRot()));

            RandomSource random = serverLevel.getRandom();

            // Left thruster bubbles
            if (hasLeftThruster && (c$this.isSwimming() || c$this.isAutoSpinAttack())) {
                double offsetX = forwardZ * 0.25;
                double offsetZ = -forwardX * 0.25;
                for (int i = 0; i < 1; i++) {
                    serverLevel.sendParticles(ParticleTypes.BUBBLE,
                            player.getX() + offsetX + random.nextGaussian() * 0.05,
                            baseY + random.nextGaussian() * 0.05,
                            player.getZ() + offsetZ + random.nextGaussian() * 0.05,
                            1, 0, 0, 0, 0.02);
                }
            }

            // Right thruster bubbles
            if (hasRightThruster && c$this.isSwimming() || c$this.isAutoSpinAttack()) {
                double offsetX = -forwardZ * 0.25;
                double offsetZ = forwardX * 0.25;
                for (int i = 0; i < 1; i++) {
                    serverLevel.sendParticles(ParticleTypes.BUBBLE,
                            player.getX() + offsetX + random.nextGaussian() * 0.05,
                            baseY + random.nextGaussian() * 0.05,
                            player.getZ() + offsetZ + random.nextGaussian() * 0.05,
                            1, 0, 0, 0, 0.02);
                }
            }
        }

    }
}
