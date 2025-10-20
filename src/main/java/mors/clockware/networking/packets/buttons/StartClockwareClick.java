package mors.clockware.networking.packets.buttons;

import io.netty.buffer.ByteBuf;
import mors.clockware.Clockware_Main;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record StartClockwareClick(boolean rightClick, int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StartClockwareClick> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_start_click"));

    public static final StreamCodec<ByteBuf, StartClockwareClick> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    StartClockwareClick::rightClick,
                    ByteBufCodecs.INT,
                    StartClockwareClick::entityId,
                    StartClockwareClick::new
            );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
