package mors.clockware.registry;

import mors.clockware.Clockware_Registries;
import mors.clockware.client.screen.ClockwareMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class Clockware_Screens {
    public static DeferredHolder<MenuType<?>, MenuType<ClockwareMenu>> CLOCKWARE;

    public static void init() {
        CLOCKWARE = register("clockware", () -> IMenuTypeExtension.create(new ClockwareMenu.Factory()));
    }

    public static<T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(final String name, final Supplier<MenuType<T>> item) {
        return Clockware_Registries.MENUS.register(name, item);
    }
}
