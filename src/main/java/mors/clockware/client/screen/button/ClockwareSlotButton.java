package mors.clockware.client.screen.button;

import com.simibubi.create.AllSoundEvents;
import mors.clockware.Clockware_Main;
import mors.clockware.item.ClockwareItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ClockwareSlotButton extends Button {

    public ItemStack clockware=ItemStack.EMPTY;

    public ClockwareSlotButton(int x, int y) {
        super(x, y, 16, 16, CommonComponents.EMPTY, button -> {}, DEFAULT_NARRATION);
    }

    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(AllSoundEvents.CRAFTER_CLICK.getMainEvent(), 1.0F));
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 300.0F);
        if(!active){
            guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "icon/remove"),
                    16, 16,
                    0, 0,
                    this.getX(), this.getY(),
                    16, 16);
        }
        guiGraphics.pose().popPose();

        renderSlot(guiGraphics, clockware, this.getX(), this.getY(), mouseX, mouseY);
    }

    protected void renderSlot(GuiGraphics guiGraphics, ItemStack itemstack, int x, int y, int mouseX, int mouseY) {
        if(itemstack.isEmpty()) return;

        int slotWidth = 16;
        int slotHeight = 16;

        boolean selected = mouseX >= x && mouseY >= y && mouseX < x + slotWidth && mouseY < y + slotHeight;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

        guiGraphics.renderFakeItem(itemstack, x, y, 0);

        if (selected) {
            //Overlay
            guiGraphics.fill(RenderType.guiOverlay(), x, y, x + slotWidth, y + slotHeight, 0x80FFFFFF);

            //Tooltip
            Minecraft mc = Minecraft.getInstance();
            var tooltip = Screen.getTooltipFromItem(mc, itemstack);

            //Replace the "Install with a Ripperdoc" tooltip
            tooltip.replaceAll(c -> {
                if (Objects.equals(c.getStyle().getInsertion(), "clockware:install_line")
                        && itemstack.getItem() instanceof ClockwareItem clockwareItem) {
                    return Component.translatable("gui.clockware.ripperdoc.price.install",clockwareItem.getInstallPrice()).withStyle(ChatFormatting.GREEN);
                }

                return c;
            });

            guiGraphics.renderTooltip(mc.font, tooltip, itemstack.getTooltipImage(), itemstack, mouseX, mouseY);
        }

        guiGraphics.pose().popPose();
    }

    public void setClockware(ItemStack clockware) {
        this.clockware = clockware;
    }
}
