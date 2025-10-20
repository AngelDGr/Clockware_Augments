package mors.clockware.networking.packets.buttons;

import io.netty.buffer.ByteBuf;
import mors.clockware.Clockware_Main;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record DoubleJumpClockware(int entityId) implements CustomPacketPayload {
    public static final Type<DoubleJumpClockware> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_double_jump"));

    public static final StreamCodec<ByteBuf, DoubleJumpClockware> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            DoubleJumpClockware::entityId,
            DoubleJumpClockware::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
