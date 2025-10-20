package mors.clockware.item;

import mors.clockware.item.component.ProjectileArmStorage;
import mors.clockware.registry.Clockware_Components;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ProjectileLauncherArm extends ClockwareItem {
    public ProjectileLauncherArm(ClockwareType clockwareType, int level, String clockwareName, int installPrice) {

        super(new Item.Properties().stacksTo(1)
                        .rarity(level==0? Rarity.COMMON: level==1? Rarity.UNCOMMON: Rarity.RARE)
                        .component(Clockware_Components.PROJECTILE_ARM_STORAGE, ProjectileArmStorage.createStorage(64 + (level*64))),

                clockwareType, level, clockwareName, installPrice, UseAnim.CUSTOM, "", true);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) {
            return false;
        } else {
            ProjectileArmStorage projectileArmStorage = stack.get(Clockware_Components.PROJECTILE_ARM_STORAGE);
            if (projectileArmStorage == null) {
                return false;
            } else {
                ItemStack itemstack = slot.getItem();
                ProjectileArmStorage.Mutable mutable = new ProjectileArmStorage.Mutable(projectileArmStorage);
                if (itemstack.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack itemstack1 = mutable.removeOne();
                    if (itemstack1 != null) {
                        ItemStack itemstack2 = slot.safeInsert(itemstack1);
                        mutable.tryInsert(itemstack2);
                    }
                } else if (ProjectileArmStorage.Mutable.canInsert(slot.getItem())) {
                    int i = mutable.tryTransfer(slot, player);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(Clockware_Components.PROJECTILE_ARM_STORAGE, mutable.toImmutable());
                return true;
            }
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, @NotNull ItemStack other, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {

        if (stack.getCount() != 1) return false;
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {

            ProjectileArmStorage projectileArmStorage = stack.get(Clockware_Components.PROJECTILE_ARM_STORAGE);
            if (projectileArmStorage == null) {
                return false;
            } else {
                ProjectileArmStorage.Mutable storageMutable = new ProjectileArmStorage.Mutable(projectileArmStorage);
                if (other.isEmpty()) {
                    ItemStack itemstack = storageMutable.removeOne();
                    if (itemstack != null) {
                        this.playRemoveOneSound(player);
                        access.set(itemstack);
                    }
                } else {
                    int i = storageMutable.tryInsert(other);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(Clockware_Components.PROJECTILE_ARM_STORAGE, storageMutable.toImmutable());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        ProjectileArmStorage projectileArmStorage = stack.get(Clockware_Components.PROJECTILE_ARM_STORAGE);
        if (projectileArmStorage != null) {
            int amount=0;
            for (ItemStack stored : projectileArmStorage.items()) amount+=stored.getCount();

            tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", amount, projectileArmStorage.maxItems()).withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(Clockware_Components.PROJECTILE_ARM_STORAGE))
                : Optional.empty();
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }


    public static void unsuccessfulShoot(Level level, LivingEntity shooter, HumanoidArm arm) {
        spawnParticles(level, shooter, arm, ParticleTypes.SMOKE, SoundEvents.DISPENSER_FAIL);
    }

    public static void spawnParticles(Level level, LivingEntity shooter, HumanoidArm arm, ParticleOptions particle, @Nullable SoundEvent sound) {
        // Position slightly on the arm side
        Vec3 eyePos = shooter.getEyePosition(1.0F);
        float sideOffset = 0.35F;
        float forwardOffset = 0.5F;
        float verticalOffset = -0.4F;

        Vec3 lookVec = shooter.getViewVector(1.0F);
        Vec3 rightVec = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 upVec = rightVec.cross(lookVec).normalize();


        float sideMultiplier = arm == HumanoidArm.LEFT ? -1 : 1;


        Vec3 spawnPos = eyePos
                .add(rightVec.scale(sideOffset * sideMultiplier))
                .add(lookVec.scale(forwardOffset))
                .add(upVec.scale(verticalOffset));

        final int count = 6;
        final double spread = 0.12;
        final double speed = 0.02;

        if (level.isClientSide) {
            for (int i = 0; i < count; i++) {
                double dx = spawnPos.x;
                double dy = spawnPos.y;
                double dz = spawnPos.z;
                level.addParticle(particle, spawnPos.x, spawnPos.y, spawnPos.z, dx, dy, dz);
            }

            if(sound!=null)
                level.playLocalSound(spawnPos.x, spawnPos.y, spawnPos.z, sound, SoundSource.PLAYERS, 0.5F, 1.0F, false);
        } else if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, count, spread, spread, spread, speed);

            if(sound!=null)
                serverLevel.playSound(null, spawnPos.x, spawnPos.y, spawnPos.z, sound, SoundSource.PLAYERS, 0.5F, 1.0F);
        }
    }

    public static void performShooting(
            Level level, LivingEntity shooter, HumanoidArm arm, ItemStack ammo, float velocity, float inaccuracy) {
        if (level instanceof ServerLevel serverlevel) {

            shoot(serverlevel, shooter, ammo, arm, velocity, inaccuracy, shooter instanceof Player);

            spawnParticles(level, shooter, arm, ParticleTypes.SMOKE, null);
        }
    }

    protected static void shoot(
            ServerLevel level,
            LivingEntity shooter,
            ItemStack projectileItem,
            HumanoidArm arm,
            float velocity,
            float inaccuracy,
            boolean isCrit
    ) {

        if (!projectileItem.isEmpty()) {

            Projectile projectile = createProjectile(level, shooter, projectileItem, isCrit);
            if(projectile!=null){
                shootProjectile(shooter, projectile, arm, velocity, inaccuracy);
                level.addFreshEntity(projectile);
            }
        }
    }

    protected static void shootProjectile(
            LivingEntity shooter, Projectile projectile, HumanoidArm arm, float velocity, float inaccuracy
    ) {
        Vector3f vector3f;

        setProjectilePositon(shooter, projectile, arm);

        Vec3 vec3 = shooter.getUpVector(1.0F);
        Quaternionf quaternionf = new Quaternionf().setAngleAxis((float) 0 * (float) (Math.PI / 180.0), vec3.x, vec3.y, vec3.z);
        Vec3 vec31 = shooter.getViewVector(1.0F);
        vector3f = vec31.toVector3f().rotate(quaternionf);

        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);
        shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.DISPENSER_LAUNCH, shooter.getSoundSource(), 1.0F, 1.0F);
    }


    protected static @Nullable Projectile createProjectile(Level level, LivingEntity shooter, ItemStack ammo, boolean isCrit) {
        ArrowItem arrowitem = ammo.getItem() instanceof ArrowItem arrowitem1 ? arrowitem1 : null;

        if(arrowitem!=null){
            AbstractArrow abstractarrow = arrowitem.createArrow(level, ammo, shooter, null);
            if (isCrit) {
                abstractarrow.setCritArrow(true);
            }

            return abstractarrow;
        } else if (ammo.getItem() instanceof ProjectileItem projectileItem) {
            return projectileItem.asProjectile(level,  shooter.getOnPos().getCenter(), ammo, shooter.getDirection());
        }

        return null;
    }

    private static void setProjectilePositon(LivingEntity shooter, Projectile projectile, HumanoidArm arm){
        // Position slightly on the arm side
        Vec3 eyePos = shooter.getEyePosition(1.0F);
        float sideOffset = 0.35F;
        float forwardOffset = 0F;
        float verticalOffset = -0.4F;

        Vec3 lookVec = shooter.getViewVector(1.0F);
        Vec3 rightVec = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 upVec = rightVec.cross(lookVec).normalize();

        float sideMultiplier = arm == HumanoidArm.LEFT ? -1 : 1;

        Vec3 spawnPos = eyePos
                .add(rightVec.scale(sideOffset * sideMultiplier))
                .add(lookVec.scale(forwardOffset))
                .add(upVec.scale(verticalOffset));

        projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
    }
}
