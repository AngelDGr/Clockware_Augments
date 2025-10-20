package mors.clockware.mixin.clockware;

import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

public class ShockAbsorberMixins {

    @Mixin(LivingEntity.class)
    public abstract static class LivingEntityMixin extends Entity implements Attackable, ILivingEntityExtension {

        public LivingEntityMixin(EntityType<?> entityType, Level level) {
            super(entityType, level);
        }

        @Unique
        public final LivingEntity c$this = (LivingEntity) (Object) this;

        @ModifyVariable(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"), ordinal = 0)
        private double clockware$increasesGravity(double constant) {
            boolean falling = this.getDeltaMovement().y <= 0.0;
            if (!falling || c$this.isFallFlying()) return constant;

            double addedGravity = 0.0;

            // Check right leg
            if (Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_LEG, "shock_absorber", c$this.clockware$getClockwareInventory())) {
                ItemStack rightLegStack = c$this.clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_LEG);
                if (rightLegStack.getItem() instanceof ClockwareItem clockwareRight) {
                    addedGravity += (clockwareRight.getLevel() + 1) * 0.01;
                }
            }

            // Check left leg
            if (Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_LEG, "shock_absorber", c$this.clockware$getClockwareInventory())) {
                ItemStack leftLegStack = c$this.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_LEG);
                if (leftLegStack.getItem() instanceof ClockwareItem clockwareLeft) {
                    addedGravity += (clockwareLeft.getLevel() + 1) * 0.01;
                }
            }

            return constant + addedGravity;
        }

        @ModifyArg(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
        private float clockware$reduceDamage(float original) {
            float reduction=0;
            int rightLevel=0;
            int leftLevel=0;

            if (Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_LEG, "shock_absorber", c$this.clockware$getClockwareInventory())) {
                ItemStack rightLegStack = c$this.clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_LEG);
                if (rightLegStack.getItem() instanceof ClockwareItem clockwareRight) {
                    reduction += (clockwareRight.getLevel()+1)*0.14f;
                    rightLevel=clockwareRight.getLevel();
                }
            }

            if (Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_LEG, "shock_absorber", c$this.clockware$getClockwareInventory())) {
                ItemStack leftLegStack = c$this.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_LEG);
                if (leftLegStack.getItem() instanceof ClockwareItem clockwareLeft) {
                    reduction += (clockwareLeft.getLevel()+1)*0.14f;
                    leftLevel=clockwareLeft.getLevel();
                }
            }

            //Prototype  -> -28%
            //Refined    -> -56%
            //Masterwork -> -84%
            float damageToAbsorb = original * (reduction);

            //Shockwave only with both legs
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_LEG, "shock_absorber", c$this.clockware$getClockwareInventory())
                    && Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_LEG, "shock_absorber", c$this.clockware$getClockwareInventory())){

                if(damageToAbsorb > 0){

                    //Prototype  -> 1
                    //Refined    -> 2
                    //Masterwork -> 3
                    float multiplicator= 1f + ((leftLevel + rightLevel) * 0.5f);

                    float damageToDo = damageToAbsorb * multiplicator;

                    float size = (fallDistance * (0.10f +(leftLevel * 0.05f) +(rightLevel * 0.05f)));

                    c$pushAndDamageEntities(c$this, damageToDo, 0.5f + (size), 3, multiplicator);

                    if(c$this instanceof ServerPlayer serverPlayer)
                        serverPlayer.setSpawnExtraParticlesOnFall(true);

                    c$spawnImpactParticles(this, c$this.getBoundingBox().getXsize() + (1f+(size)), fallDistance);

                    c$this.level().playSound(null, c$this.blockPosition(),
                            SoundEvents.ANVIL_LAND, SoundSource.PLAYERS,
                            1.0F, 0.8F + c$this.level().getRandom().nextFloat() * 0.4F);
                }
            }

            if(damageToAbsorb>0){
                return original - damageToAbsorb;
            }

            return original;
        }

        @Unique
        private static void c$pushAndDamageEntities(final LivingEntity pusherEntity, final float damage, final double lateralExpansion, final double yExpansion, final double knockbackStrength, final Class<?>... classException){
            c$pushAndDamageEntities(pusherEntity, damage, lateralExpansion, yExpansion, knockbackStrength,
                    pusherEntity instanceof Player player? pusherEntity.damageSources().playerAttack(player): pusherEntity.damageSources().mobAttack(pusherEntity),
                    classException);
        }

        /**
         Method to push and damage enemies, disable player shield and destroy End Crystals, Vehicles and Item Frames, for entities that aren't mobs
         */
        @Unique
        private static void c$pushAndDamageEntities(final Entity pusherEntity, final float damage, final double lateralExpansion, final double yExpansion, final double knockbackStrength, final DamageSource damageSource, final Class<?>... classException){
            final List<Entity> listMobs= pusherEntity.level().getEntitiesOfClass(Entity.class, pusherEntity.getBoundingBox().inflate(lateralExpansion,yExpansion,lateralExpansion),
                    entity -> {
                        for (final Class<?> class_ : classException) {
                            if (class_.isAssignableFrom(entity.getClass()))
                                return false;
                        }
                        return entity != pusherEntity;
                    }
            );


            for (final Entity entity : listMobs){
                if (entity == pusherEntity) continue;

                // --- Line of sight check ---
                final Vec3 startPos = pusherEntity.position().add(0, pusherEntity.getEyeHeight() * 0.5, 0);
                final Vec3 targetPos = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
                final ClipContext context = new ClipContext(
                        startPos,
                        targetPos,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        pusherEntity
                );

                final BlockHitResult result = pusherEntity.level().clip(context);

                // Skip entity if something solid blocks the view
                if (result.getType() == HitResult.Type.BLOCK &&
                        result.getBlockPos() != null &&
                        result.getBlockPos().closerThan(new Vec3i((int) startPos.x, (int) startPos.y, (int) startPos.z) , startPos.distanceTo(targetPos))) {
                    continue;
                }

                // --- If visible, apply effect ---
                final double d = pusherEntity.getX() - entity.getX();
                final double e = pusherEntity.getZ() - entity.getZ();

                if(entity instanceof final LivingEntity livingEntity) {

                    livingEntity.knockback(knockbackStrength, d, e);
                    //Push the player
                    if (entity instanceof ServerPlayer && !((ServerPlayer) entity).isCreative()) {
                        ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity), null);
                    }
                    //Removes the shield
                    if (livingEntity.isBlocking() && entity instanceof Player) {
                        ((Player) entity).disableShield();
                    }
                    //Checks if the entity it's blocking, to block the damage
                    else if (!livingEntity.isBlocking()) {
                        entity.hurt(damageSource, damage);
                    }

                    //Destroys other no-living entities
                } else if(entity instanceof EndCrystal) {
                    entity.hurt(damageSource, 50.0f);
                } else {
                    return;
                }
            }
        }

        /**
         Spawns particles with the texture of the block where it impacts, in a circle shape
         */
        @Unique
        private static void c$spawnImpactParticles(final Entity entity, final double radius, final double fallingDistance) {

            double pQuantity=Math.max(80 + fallingDistance, 2 * Math.PI * radius);

            // To get the ground position
            final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(entity.getOnPos().getX(), entity.getOnPos().getY(), entity.getOnPos().getZ());
            while (entity.level().getBlockState(pos).isAir() && pos.getY()>-64) {
                pos.setY(pos.getY() - 1);
            }

            pos.setY(pos.getY() + 1);

            if(entity.isInWater()) pos.set(entity.getOnPos());

            // Fill the circle with particles
            final double stepSize = radius / 10.0;
            for (double r = 0; r <= radius; r += stepSize) {
                final double particlesInRing = Math.max(pQuantity, 2 * Math.PI * r);
                for (int i = 0; i < particlesInRing; i++) {
                    final double angle = (2 * Math.PI) * i / particlesInRing;
                    final double offsetX = r * Math.cos(angle);
                    final double offsetZ = r * Math.sin(angle);

                    // Add some vertical randomness for particle height
                    final double d = entity.level().getRandom().nextGaussian() * 0.5;
                    final double e = entity.level().getRandom().nextGaussian() * 0.5;
                    final double f = entity.level().getRandom().nextGaussian() * 0.5;



                    // Use a block particle for the interior
                    final BlockState blockState = entity.level().getBlockState(
                            new BlockPos(
                                    (int) (entity.getX()+offsetX),
                                    pos.below().getY(),
                                    (int) (entity.getZ()+offsetZ)));
                    if(blockState.getRenderShape() != RenderShape.INVISIBLE) {
                        // Select the particle type
                        final ParticleOptions particleType = new BlockParticleOption(ParticleTypes.BLOCK, blockState);

                        // Spawn the particle at the calculated position
                        entity.level().addParticle(particleType,
                                entity.getX() + offsetX,
                                pos.getY(),
                                entity.getZ() + offsetZ,
                                d, e, f);
                    }
                }
            }
        }
    }
}
