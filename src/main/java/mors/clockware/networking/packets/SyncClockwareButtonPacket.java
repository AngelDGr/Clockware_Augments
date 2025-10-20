package mors.clockware.networking.packets;

import mors.clockware.Clockware_Main;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record SyncClockwareButtonPacket(ItemStack clockware, int clockwareSlot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncClockwareButtonPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_button_item"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncClockwareButtonPacket> STREAM_CODEC = StreamCodec.of(
            SyncClockwareButtonPacket::writeToStream,
            SyncClockwareButtonPacket::createFromStream
    );

    private static void writeToStream(RegistryFriendlyByteBuf buffer, SyncClockwareButtonPacket packet) {
        if(packet.clockware.isEmpty())
            buffer.writeBoolean(false);
        else {
            buffer.writeBoolean(true);
            ItemStack.STREAM_CODEC.encode(buffer, packet.clockware);
        }

        buffer.writeInt(packet.clockwareSlot);
    }

    public static SyncClockwareButtonPacket createFromStream(RegistryFriendlyByteBuf buffer) {
        ItemStack stack = ItemStack.EMPTY;

        if(buffer.readBoolean())
            stack = ItemStack.STREAM_CODEC.decode(buffer);

        int clockwareSlot = buffer.readInt();

        return new SyncClockwareButtonPacket(stack, clockwareSlot);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
