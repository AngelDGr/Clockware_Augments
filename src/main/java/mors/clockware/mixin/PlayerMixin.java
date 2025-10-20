package mors.clockware.mixin;

import mors.clockware.injection.IsRightClickingClockware;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IsRightClickingClockware {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) { super(entityType, level);}

    @Unique
    private static final EntityDataAccessor<Boolean> c$RightClicking = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void clockware$IsRightClicking(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(c$RightClicking, false);
    }

    @Override
    public boolean clockware$isRightClicking() {
        return this.entityData.get(c$RightClicking);
    }

    @Override
    public void clockware$setIsRightClicking(boolean isRightClicking) {
        this.entityData.set(c$RightClicking, isRightClicking);
    }
}
