package mors.clockware.registry;

import mors.clockware.Clockware_Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;

public class Clockware_Tags {

    public static final TagKey<Item> CLOCKWARE_ITEMS = createTag(Registries.ITEM, "clockware_items");

    public static final TagKey<Item> PROTOTYPE_CLOCKWARE = createTag(Registries.ITEM, "prototype_clockware");
    public static final TagKey<Item> REFINED_CLOCKWARE = createTag(Registries.ITEM, "refined_clockware");
    public static final TagKey<Item> MASTERWORK_CLOCKWARE = createTag(Registries.ITEM, "masterwork_clockware");

    public static final TagKey<MobEffect> OPTICAL_CALIBRATOR_EFFECT_IMMUNITY = createTag(Registries.MOB_EFFECT, "optical_calibrator_effect_immunity");

    private static <T> TagKey<T> createTag(ResourceKey<Registry<T>> registryResourceKey, String name) {
        return createTag(registryResourceKey, ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, name));
    }

    private static <T> TagKey<T> createTag(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation resourceLocation) {
        return TagKey.create(registryResourceKey, resourceLocation);
    }
}
