package mors.clockware.client.screen.widget;

import mors.clockware.Clockware_Main;
import mors.clockware.client.screen.ClockwareScreen;
import mors.clockware.client.screen.button.ClockwareSlotButton;
import mors.clockware.client.screen.button.SimpleButton;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.networking.packets.SyncClockwareButtonPacket;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClockwareListWidget extends Screen implements GuiEventListener {
    protected static final ResourceLocation CLOCKWARE_LIST = ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "textures/gui/sprites/widget/clockware_list.png");
    private int leftPos;
    private int topPos;
    private boolean visible;
    private final ClockwareScreen mainScreen;
    public NonNullList<ItemStack> clockwareList = NonNullList.withSize(22, ItemStack.EMPTY);
    private final List<ClockwareSlotButton> clockwareSlotButtons = new ArrayList<>();
    private SimpleButton rewindButton;
    private SimpleButton removeButton;

    public ClockwareListWidget(ClockwareScreen mainScreen) {
        super(Component.empty());
        this.mainScreen=mainScreen;
    }

    @Override
    protected void init() {
        int i = (this.leftPos - 71);

        int columns = 3;
        int slotSeparation = 22;
        int startX = i + 7;
        int startY = this.topPos + 30;

        for (int m = 0; m < clockwareList.size(); m++) {
            int col = m % columns;
            int row = m / columns;

            int xOffset = startX + col * slotSeparation;
            int yOffset = startY + row * slotSeparation;

            if(m==21){
                clockwareSlotButtons.add(new ClockwareSlotButton(i+29,  this.topPos+184));
            } else {
                clockwareSlotButtons.add(new ClockwareSlotButton(xOffset, yOffset));
            }
        }

        this.rewindButton=new SimpleButton(i+6, this.topPos+183);
        this.rewindButton.iconName="rewind";
        this.removeButton=new SimpleButton(i+50, this.topPos+183);
        this.removeButton.iconName="negate";
    }


    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        //Sort the clockware list
        clockwareList.sort((a, b) -> {
            if (a.isEmpty() && !b.isEmpty()) return 1;  // Empty items go last
            if (!a.isEmpty() && b.isEmpty()) return -1;
            if (a.isEmpty()) return 0;

            // If both are ClockwareItems, compare their internal level
            if (a.getItem() instanceof ClockwareItem ca && b.getItem() instanceof ClockwareItem cb) {
                //If they are the same clockware, they are compared by level
                if(ca.getClockwareName().equals(cb.getClockwareName()))
                    return -Integer.compare(cb.getLevel(), ca.getLevel());
                //If they are different clockware, they are compared by alphabetic name
                else
                    return -CharSequence.compare(cb.getClockwareName(), ca.getClockwareName());
            }

            return 0;
        });

        if(this.isVisible()){
            int i = (this.leftPos - 71);
            int j = (this.topPos);

            guiGraphics.blit(CLOCKWARE_LIST, i, j, 0, 0, 74, 219);

            //Reposition buttons to work when change of resolution
            int columns = 3;
            int slotSeparation = 22;
            int startX = i + 7;
            int startY = this.topPos + 30;

            for (int m = 0; m < clockwareList.size(); m++) {
                int col = m % columns;
                int row = m / columns;

                int xOffset = startX + col * slotSeparation;
                int yOffset = startY + row * slotSeparation;

                clockwareSlotButtons.get(m).setClockware(clockwareList.get(m));
                clockwareSlotButtons.get(m).render(guiGraphics, mouseX, mouseY, partialTick);

                if(m==21)
                    clockwareSlotButtons.get(m).setPosition(i+29,  this.topPos+184);
                else
                    clockwareSlotButtons.get(m).setPosition(xOffset, yOffset);
            }

            rewindButton.setPosition(i+6, this.topPos+183);
            rewindButton.render(guiGraphics, mouseX, mouseY, partialTick);
            removeButton.setPosition(i+50, this.topPos+183);
            removeButton.render(guiGraphics, mouseX, mouseY, partialTick);

            for(ClockwareSlotButton b : clockwareSlotButtons){
                b.visible= b.clockware != ItemStack.EMPTY;
                b.active=true;
                // Check if this exact stack is already installed
                for (int m = 0; m < this.mainScreen.getMenu().getClockwareContainers().getContainerSize(); m++) {
                    ItemStack installed = this.mainScreen.getMenu().getClockwareContainers().getItem(m);

                    if (!installed.isEmpty() && ItemStack.isSameItemSameComponents(installed, b.clockware)) {
                        b.active = false; // Deactivate because it's installed
                        break; // No need to check other slots
                    }
                }
            }


            Component component = Component.translatable("gui.clockware.ripperdoc.part."+this.mainScreen.currentClockwareSlotType.getName());
            guiGraphics.drawString(this.font, component, Clockware_Util.centerTextX(Minecraft.getInstance(), component, this.leftPos-34), this.topPos+ 208, 0x442000, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ClockwareSlotButton b : clockwareSlotButtons) {
            if (b.mouseClicked(mouseX, mouseY, button)){
                this.mainScreen.mannequin.clockware$setClockwareBySlot(this.mainScreen.currentClockwareSlotType, b.clockware);

                //Sync with server
                PacketDistributor.sendToServer(new SyncClockwareButtonPacket(b.clockware, this.mainScreen.currentClockwareSlotType.getIndex()));

                return true;
            }
        }

        //Remove button
        if(removeButton.mouseClicked(mouseX, mouseY, button)){
            this.mainScreen.mannequin.clockware$setClockwareBySlot(this.mainScreen.currentClockwareSlotType, ItemStack.EMPTY);

            //Sync with server
            PacketDistributor.sendToServer(new SyncClockwareButtonPacket(ItemStack.EMPTY, this.mainScreen.currentClockwareSlotType.getIndex()));

            return true;
        }
        //Rewind button
        else if(rewindButton.mouseClicked(mouseX, mouseY, button)){
            for(ClockwareSlotType slotType: ClockwareSlotType.values()){
                this.mainScreen.mannequin.clockware$setClockwareBySlot(
                        slotType,
                        this.mainScreen.getMenu().initClockware.get(slotType.getIndex()));

                //Sync with server
                PacketDistributor.sendToServer(new SyncClockwareButtonPacket(
                        this.mainScreen.getMenu().initClockware.get(slotType.getIndex()),
                        slotType.getIndex()));
            }

            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void setClockwareList(NonNullList<ItemStack> clockwareList) {
        this.clockwareList = clockwareList;
    }

    public void setPos(int width, int height){
        this.leftPos = width;
        this.topPos = height;
    }

    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }

    public boolean isVisible() {
        return this.visible;
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return false;
    }
}
