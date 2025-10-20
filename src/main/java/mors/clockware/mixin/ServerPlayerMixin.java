package mors.clockware.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Unique
    ServerPlayer c$this = (ServerPlayer)(Object) this;

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void clockware$copyClockwareInventoryAfterDead(ServerPlayer oldPlayer, boolean keepEverything, CallbackInfo ci) {
        for (int i = 0; i < oldPlayer.clockware$getClockwareInventory().size(); i++) {
            c$this.clockware$getClockwareInventory().set(i, oldPlayer.clockware$getClockwareInventory().get(i).copy());
        }
    }
}
