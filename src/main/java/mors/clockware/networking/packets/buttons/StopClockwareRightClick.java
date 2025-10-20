package mors.clockware.networking.packets.buttons;

import io.netty.buffer.ByteBuf;
import mors.clockware.Clockware_Main;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record StopClockwareRightClick(int useTime, boolean rightClick, int entityId) implements CustomPacketPayload {
    public static final Type<StopClockwareRightClick> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_finish_click"));

    public static final StreamCodec<ByteBuf, StopClockwareRightClick> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            StopClockwareRightClick::useTime,
            ByteBufCodecs.BOOL,
            StopClockwareRightClick::rightClick,
            ByteBufCodecs.INT,
            StopClockwareRightClick::entityId,
            StopClockwareRightClick::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
