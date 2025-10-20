package mors.clockware.networking.packets;

import io.netty.buffer.ByteBuf;
import mors.clockware.Clockware_Main;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SyncVillagerProfession(String profession) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncVillagerProfession> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "sync_profession"));

    public static final StreamCodec<ByteBuf, SyncVillagerProfession> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SyncVillagerProfession::profession,
            SyncVillagerProfession::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
