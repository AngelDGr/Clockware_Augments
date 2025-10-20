package mors.clockware.client.screen.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.AllSoundEvents;
import mors.clockware.Clockware_Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SimpleButton extends Button {

    public String iconName="none";
    private final SoundEvent pressSound;

    public SimpleButton(int x, int y) {
        this(x, y, button -> {},  AllSoundEvents.CRAFTER_CLICK.getMainEvent());
    }

    public SimpleButton(int x, int y, OnPress onPress, SoundEvent pressSound) {
        super(x, y, 18, 18, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.pressSound  = pressSound;
    }

    protected static final WidgetSprites SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "widget/button"),
            ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "widget/button_disabled"),
            ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "widget/button_highlighted")
    );

    @Override
    public void playDownSound(@NotNull SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(this.pressSound, 1.0F));
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "icon/"+iconName),
                18, 18,
                0, 0,
                this.getX(), this.getY(),
                18, 18);
    }
}
