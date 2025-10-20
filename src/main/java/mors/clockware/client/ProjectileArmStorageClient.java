package mors.clockware.client;

import mors.clockware.Clockware_Main;
import mors.clockware.item.component.ProjectileArmStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProjectileArmStorageClient implements ClientTooltipComponent {
    public static final ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "textures/gui/sprites/widget/projectile_slot.png");

    private final List<ItemStack> items;

    public ProjectileArmStorageClient(ProjectileArmStorage storage) {
        this.items = storage.items();
        int maxItems = storage.maxItems();
    }

    @Override
    public int getHeight() {
        return getRows() * 18 + 2;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return getColumns() * 18 + 2;
    }

    private int getColumns() {
        return 9;
    }

    private int getRows() {
        return Mth.ceil((float) items.size() / getColumns());
    }


    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
        int columns = getColumns();

        for (int i = 0; i < items.size(); ++i) {
            int row = i / columns;
            int col = i % columns;
            int slotX = x + col * 18 + 1;
            int slotY = y + row * 18 + 1;

            guiGraphics.blit(SLOT, slotX-1, slotY-1, 0, 0, 20, 20, 20, 20);

            guiGraphics.renderItem(items.get(i), slotX, slotY);

            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, items.get(i), slotX, slotY);
        }
    }
}
