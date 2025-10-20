package mors.clockware.client.screen;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.neoforged.neoforge.client.extensions.IMenuProviderExtension;
import org.jetbrains.annotations.NotNull;

public final class ClockwareMenuProvider implements MenuProvider, IMenuProviderExtension {
    private final Component title;
    private final MenuConstructor menuConstructor;
    private final int villagerId;

    public ClockwareMenuProvider(MenuConstructor menuConstructor, Component title, int villagerId) {
        this.menuConstructor = menuConstructor;
        this.title = title;
        this.villagerId=villagerId;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.title;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return this.menuConstructor.createMenu(containerId, playerInventory, player);
    }

    @Override
    public void writeClientSideData(@NotNull AbstractContainerMenu menu, @NotNull RegistryFriendlyByteBuf buffer) {
        if(villagerId == -1){
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeInt(villagerId);
        }
    }
}
