package mors.clockware.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonPacketListenerImpl implements TickablePacketListener, ClientGamePacketListener {
    @Shadow private ClientLevel level;

    public ClientPlayNetworkHandlerMixin(final Minecraft client, final Connection connection, final CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "handleRespawn", at = @At("TAIL"))
    private void injectChangesInEyesRespawn(final ClientboundRespawnPacket packet, final CallbackInfo ci){

    }
}
