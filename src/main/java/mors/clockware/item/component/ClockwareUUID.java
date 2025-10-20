package mors.clockware.item.component;

import com.mojang.serialization.Codec;
import mors.clockware.registry.Clockware_Components;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public record ClockwareUUID(UUID uuid) {
    public static final ClockwareUUID DEFAULT = new ClockwareUUID(UUID.fromString("63018d9e-d36a-48b0-8314-28e9d6f80e35"));

    public static final Codec<ClockwareUUID> CODEC = Codec.STRING.xmap(
            UUID::fromString,
            UUID::toString).xmap(ClockwareUUID::new, ClockwareUUID::uuid);


    public static ItemStack of(final ItemStack stack, UUID uuid){
        stack.set(Clockware_Components.CLOCKWARE_UUID, new ClockwareUUID(uuid));

        return stack;
    }
}
