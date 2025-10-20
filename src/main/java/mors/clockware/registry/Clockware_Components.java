package mors.clockware.registry;

import com.mojang.serialization.Codec;
import mors.clockware.Clockware_Registries;
import mors.clockware.item.component.ClockwareUUID;
import mors.clockware.item.component.ProjectileArmStorage;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Clockware_Components {
    //This makes each clockware unique, the only way I found to no being able to install the same clockware stack two times
    public static Supplier<DataComponentType<ClockwareUUID>> CLOCKWARE_UUID;

    //Projectile storage, similar to bundle components
    public static Supplier<DataComponentType<ProjectileArmStorage>> PROJECTILE_ARM_STORAGE;

    //To detect if the blade on a component is out or not
    public static Supplier<DataComponentType<Boolean>> BLADE_OUT;

    public static void init(){
        CLOCKWARE_UUID = register("clockware_uuid", builder -> builder.persistent(ClockwareUUID.CODEC));

        PROJECTILE_ARM_STORAGE = register("projectile_arm_storage", builder -> builder.persistent(ProjectileArmStorage.CODEC));

        BLADE_OUT = register("reaper_blade_out", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    }

    private static <T> Supplier<DataComponentType<T>> register(final String id, final UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Clockware_Registries.DATA_COMPONENTS.registerComponentType(id, builderOperator);
    }
}
