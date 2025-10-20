package mors.clockware.registry;

import mors.clockware.Clockware_Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.NotNull;

public class Clockware_DamageTypes {

//    public static ResourceKey<DamageType> SHARK_TOOTH = register("shark_tooth");

    public static void boostrapDamageTypes(@NotNull final BootstrapContext<DamageType> damageTypeRegisterable) {

    }

    public static ResourceKey<DamageType> register(final String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, name));
    }
}
