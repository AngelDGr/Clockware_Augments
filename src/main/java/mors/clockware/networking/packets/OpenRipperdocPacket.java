package mors.clockware.networking.packets;

import io.netty.buffer.ByteBuf;
import mors.clockware.Clockware_Main;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record OpenRipperdocPacket() implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<OpenRipperdocPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "open_ripperdoc"));

    public static final StreamCodec<ByteBuf, OpenRipperdocPacket> STREAM_CODEC =
            StreamCodec.unit(new OpenRipperdocPacket());
    
    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}