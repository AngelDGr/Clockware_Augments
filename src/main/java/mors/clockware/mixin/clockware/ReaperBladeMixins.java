package mors.clockware.mixin.clockware;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import mors.clockware.injection.BladeLunge;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.item.ReaperBladeArm;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

public class ReaperBladeMixins {

    @Mixin(LivingEntity.class)
    public static class LivingEntityMixin {

        //Ignore armor
        @ModifyArgs(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"))
        private void clockware$injectArmorPenetration(final Args args, @Local(argsOnly = true) DamageSource source){
            Entity entity = source.getEntity();

            if(entity instanceof LivingEntity attacker
                    && attacker.clockware$getClockwareOnMainHand().getItem() instanceof ReaperBladeArm reaperBladeArm
                    && reaperBladeArm.doSweep(attacker.clockware$getClockwareOnMainHand(), attacker, ClockwareSlotType.getMainArmSlot(attacker))){
                args.set(3, 0.0f);
                args.set(4, 0.0f);
            }
        }
    }

    @Mixin(ServerPlayer.class)
    public abstract static class ServerPlayerMixin extends Player implements BladeLunge {

        public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
            super(level, pos, yRot, gameProfile);
        }

        @Unique
        ServerPlayer c$this = (ServerPlayer)(Object) this;

        @Unique
        int c$lunge_ticks = 0;

        @Inject(method = "doTick", at = @At("TAIL"))
        private void clockware$doLungeDamage(CallbackInfo ci) {
            ItemStack mainReaperBlade = c$this.clockware$getClockwareOnMainHand();
            ItemStack offReaperBlade = c$this.clockware$getClockwareOnOffHand();

            if(mainReaperBlade.getItem() instanceof ReaperBladeArm mainClockwareItem && offReaperBlade.getItem() instanceof ReaperBladeArm offClockwareItem && c$lunge_ticks>0){
                Vec3 lookVec = c$this.getLookAngle();

                double range = 1.5D;
                //Min Level = 3 + (0*1.5) + (0*1.5) = 3
                //          = 3 + (1*1.5) + (0*1.5) = 4.5
                //          = 3 + (1*1.5) + (1*1.5) = 6
                //          = 3 + (2*1.5) + (1*1.5) = 7.5
                //Max level = 3 + (2*1.5) + (2*1.5) = 9

                float damage = 3.0F + (mainClockwareItem.getLevel() * 1.5f) + (offClockwareItem.getLevel() * 1.5f);
                Vec3 origin = c$this.position().add(0, c$this.getBbHeight() * 0.5F, 0);
//                Vec3 forward = lookVec.normalize();

                AABB hitbox = new AABB(
                        origin.x - range, origin.y - 1, origin.z - range,
                        origin.x + range, origin.y + 1, origin.z + range
                );

                List<LivingEntity> targets = c$this.level().getEntitiesOfClass(LivingEntity.class, hitbox,
                        e -> e != c$this && e.isAlive() && !e.isAlliedTo(c$this));

                for (LivingEntity target : targets) {

                    if (clockware$isSeeingTarget(target) && !target.getPersistentData().getBoolean("reaper_lunge_hit_" + c$this.getId())) {
                        target.hurt(c$this.damageSources().playerAttack(c$this), damage);
                        target.knockback(0.3F, -lookVec.x, -lookVec.z);

                        ((ServerLevel) c$this.level()).sendParticles(ParticleTypes.CRIT,
                                target.getX(), target.getY() + target.getBbHeight() * 0.5,
                                target.getZ(), 8, 0.1, 0.1, 0.1, 0.1);

                        target.getPersistentData().putBoolean("reaper_lunge_hit_" + c$this.getId(), true);
                    }
                }

                if (c$lunge_ticks == 1) {
                    c$this.level().getEntitiesOfClass(LivingEntity.class, c$this.getBoundingBox().inflate(10))
                            .forEach(e ->
                                    e.getPersistentData().remove("reaper_lunge_hit_" + c$this.getId()));
                }
            }

            if(c$lunge_ticks>0){
                c$lunge_ticks--;
            }
        }

        @Override
        public void clockware$setLungeTicks(int lunge_ticks) {
            this.c$lunge_ticks=lunge_ticks;
        }

        @Unique
        public boolean clockware$isSeeingTarget(LivingEntity target) {
            Vec3 vec3d = target.position();

            Vec3 vec3d2 = this.calculateViewVector(0.0f, this.getYHeadRot());
            Vec3 vec3d3 = vec3d.vectorTo(this.position());
            vec3d3 = new Vec3(vec3d3.x, 0.0, vec3d3.z).normalize();

            return vec3d3.dot(vec3d2) < -0.7;
        }
    }
}
