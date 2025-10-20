package mors.clockware.client.screen.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.AllItems;
import mors.clockware.Clockware_Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class InitRipperDocButton extends Button {

    public InitRipperDocButton(int x, int y, Button.OnPress onPress) {
        super(x, y, 167, 20, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.visible=false;
    }

    @Override
    public void playDownSound(@NotNull SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundEvents.PISTON_EXTEND, 1.0F));
    }

    protected static final WidgetSprites SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "widget/ripperdoc"),
            ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "widget/ripperdoc_disabled"),
            ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "widget/ripperdoc_highlighted")
    );

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        Component text=Component.translatable("gui.clockware.ripperdoc.install").setStyle(Style.EMPTY.withColor( this.isHoveredOrFocused()? 0xfae0a9 :0xf7cb6c));

        // Center text
        int textX = this.getX() + this.getWidth() / 2;
        int textY = this.getY() + (this.getHeight() - minecraft.font.lineHeight + 1) / 2;

        // Measure text width
        int textWidth = minecraft.font.width(text);

        // Compute where text starts (since we center it)
        int textStartX = textX - textWidth / 2;

        // Clockware Icon
        int iconX = (textStartX-15) - 10;
        int iconY = (this.getY()+1) + (this.getHeight() - 16) / 2; // vertically centered to button

        // Googles Item
        int gogglesX = textStartX + textWidth + 10;
        int gogglesY = this.getY() + (this.getHeight() - 16) / 2; // vertically centered to button

        //Icon, text and goggles
        guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "icon/clockware"),
                15, 15,
                0, 0,
                iconX, iconY,
                15, 15);

        guiGraphics.drawCenteredString(minecraft.font, text, textX, textY, 0xffffff);

        guiGraphics.renderFakeItem(AllItems.GOGGLES.asStack(), gogglesX, gogglesY);

        int i = getFGColor();
        this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    public void renderToolTip(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }
}