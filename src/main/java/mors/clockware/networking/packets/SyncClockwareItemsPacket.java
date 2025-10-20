package mors.clockware.networking.packets;

import mors.clockware.Clockware_Main;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record SyncClockwareItemsPacket(List<ItemStack> clockwareItems, int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncClockwareItemsPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_items"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncClockwareItemsPacket> STREAM_CODEC = StreamCodec.of(
            SyncClockwareItemsPacket::writeToStream,
            SyncClockwareItemsPacket::createFromStream
    );

    private static void writeToStream(RegistryFriendlyByteBuf buffer, SyncClockwareItemsPacket packet) {
        buffer.writeInt(packet.entityId);

        for (ItemStack stack : packet.clockwareItems()) {
            if (stack.isEmpty()) {
                buffer.writeBoolean(true); //Empty
            } else {
                buffer.writeBoolean(false); //With Something
                ItemStack.STREAM_CODEC.encode(buffer, stack);
            }
        }
    }

    public static SyncClockwareItemsPacket createFromStream(RegistryFriendlyByteBuf buffer) {
        int entityId=buffer.readInt();

        List<ItemStack> clockwareItems = new ArrayList<>();
        int sizeList = 6;

        for (int i = 0; i < sizeList; i++) {
            boolean isEmpty = buffer.readBoolean(); //Marker to detect if is empty or not
            if (isEmpty) {
                clockwareItems.add(ItemStack.EMPTY);
            } else {
                ItemStack stack = ItemStack.STREAM_CODEC.decode(buffer);
                clockwareItems.add(stack);
            }
        }

        return new SyncClockwareItemsPacket(clockwareItems, entityId);
    }


    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

