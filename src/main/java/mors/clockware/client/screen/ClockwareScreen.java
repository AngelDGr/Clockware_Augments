package mors.clockware.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import mors.clockware.Clockware_Main;
import mors.clockware.client.screen.button.SimpleButton;
import mors.clockware.client.screen.widget.ClockwareListWidget;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.item.ClockwareType;
import mors.clockware.networking.packets.SyncClockwareButtonPacket;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ClockwareScreen extends AbstractContainerScreen<ClockwareMenu> {
    private static final ResourceLocation CLOCKWARE_LOCATION = ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "textures/gui/container/clockware.png");

    private final Inventory playerInventory;
    public MannequinPlayer mannequin;
    private SimpleButton installButton;
    public final ClockwareListWidget clockwareListWidget;
    @Nullable
    public ClockwareType currentClockwareType = ClockwareType.HEAD;
    @Nullable
    public ClockwareSlotType currentClockwareSlotType = ClockwareSlotType.HEAD;

    public ClockwareScreen(ClockwareMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 232;
        this.imageHeight = 248;
        this.playerInventory=playerInventory;

        this.clockwareListWidget=new ClockwareListWidget(this);
    }

    @Override
    protected void init() {
        super.init();

        if(minecraft!=null && minecraft.level!=null && minecraft.player!=null){
            this.mannequin = new MannequinPlayer(minecraft);

            //Puts initial clockware into the mannequin
            for(ClockwareSlotType slotType: ClockwareSlotType.values()){
                mannequin.clockware$setClockwareBySlot(slotType, this.menu.getClockwareBySlot(slotType));
            }
        }

        installButton = this.addRenderableWidget(
                new SimpleButton(this.leftPos + 207, this.topPos + 224, button -> {
                    if(this.minecraft!=null && this.minecraft.gameMode!=null && this.minecraft.player!=null){
                        minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);

                        minecraft.player.closeContainer();
                    }
                }, SoundEvents.SMITHING_TABLE_USE)
        );

        this.clockwareListWidget.init(minecraft, 0, 0);

        if(this.menu.ripperdoc==null){
            currentClockwareType = null;
            currentClockwareSlotType = null;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.clockwareListWidget.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {

        if(this.minecraft!=null && this.minecraft.player!=null){
            guiGraphics.drawString(this.font, this.title, Clockware_Util.centerTextX(this.minecraft, this.title, 122), 4, 0x442000, false);

            Component head = Component.translatable("gui.clockware.ripperdoc.part.head");
            guiGraphics.drawString(this.font, head, Clockware_Util.centerTextX(this.minecraft, head, 28), 28, this.currentClockwareSlotType==ClockwareSlotType.HEAD? 16755200: 0xffffff, true);

            Component body = Component.translatable("gui.clockware.ripperdoc.part.body");
            guiGraphics.drawString(this.font, body, Clockware_Util.centerTextX(this.minecraft, body, 205), 28, this.currentClockwareSlotType==ClockwareSlotType.BODY? 16755200: 0xffffff, true);

            Component armR = Component.translatable("gui.clockware.ripperdoc.part.right_arm");
            guiGraphics.drawString(this.font, armR, Clockware_Util.centerTextX(this.minecraft, armR, 28), 106, this.currentClockwareSlotType==ClockwareSlotType.RIGHT_ARM? 16755200: 0xffffff, true);

            Component armL = Component.translatable("gui.clockware.ripperdoc.part.left_arm");
            guiGraphics.drawString(this.font, armL, Clockware_Util.centerTextX(this.minecraft, armL, 205), 106, this.currentClockwareSlotType==ClockwareSlotType.LEFT_ARM? 16755200: 0xffffff, true);

            Component legR = Component.translatable("gui.clockware.ripperdoc.part.right_leg");
            guiGraphics.drawString(this.font, legR, Clockware_Util.centerTextX(this.minecraft, legR, 28), 158, this.currentClockwareSlotType==ClockwareSlotType.RIGHT_LEG? 16755200: 0xffffff, true);

            Component legL = Component.translatable("gui.clockware.ripperdoc.part.left_leg");
            guiGraphics.drawString(this.font, legL, Clockware_Util.centerTextX(this.minecraft, legL, 205), 158, this.currentClockwareSlotType==ClockwareSlotType.LEFT_LEG? 16755200: 0xffffff, true);

            float clockware_level=0.0f;

            for (ItemStack stack: this.mannequin.clockware$getClockwareInventory()){
                if(!stack.isEmpty()){
                    clockware_level= Mth.clamp(clockware_level+ 1/6f, 0.0f, 1.0f);
                }
            }

            guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "widget/clockware_level"),
                    121, 12,
                    0, 0,
                    14, 227,
                    (int) (121*clockware_level), 12);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(CLOCKWARE_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);

        //Iterates on the player inventory to get all the clockware
        NonNullList<ItemStack> clockwareList = NonNullList.withSize(22, ItemStack.EMPTY);
        int index = 0;

        //Search on player inventory
        for (ItemStack stack : this.playerInventory.items) {
            if (stack.getItem() instanceof ClockwareItem clockwareItem &&
                    clockwareItem.getType().equals(this.currentClockwareType)) {

                if (index < clockwareList.size()) {
                    clockwareList.set(index, stack);
                    index++;
                } else {
                    break; // Stop if filled all slots
                }
            }
        }

        this.clockwareListWidget.setClockwareList(clockwareList);

        if (this.minecraft != null && this.mannequin!=null) {
            int mannequinX = i + this.imageWidth / 2;
            int mannequinY = (j + this.imageHeight / 2)+73;
            setPlayerMannequin(this.minecraft, guiGraphics, mannequin, mannequinX, mannequinY, partialTick);
        }

        this.clockwareListWidget.render(guiGraphics, mouseX, mouseY, partialTick);

        this.clockwareListWidget.setPos(this.leftPos, this.topPos);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        guiGraphics.pose().popPose();
    }

    int lastSlotIdClicked = 0;
    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if(this.menu.ripperdoc==null) return;

        super.slotClicked(slot, slotId, mouseButton, type);

        if (slotId >= 0 && slotId < 6) {
            // Determine type and slot
            switch (slotId) {
                case 0 -> {
                    this.currentClockwareType = ClockwareType.HEAD;
                    this.currentClockwareSlotType = ClockwareSlotType.HEAD;
                }
                case 1 -> {
                    this.currentClockwareType = ClockwareType.BODY;
                    this.currentClockwareSlotType = ClockwareSlotType.BODY;
                }
                case 2, 3 -> {
                    this.currentClockwareType = ClockwareType.ARM;
                    this.currentClockwareSlotType = slotId == 2 ? ClockwareSlotType.RIGHT_ARM : ClockwareSlotType.LEFT_ARM;
                }
                case 4, 5 -> {
                    this.currentClockwareType = ClockwareType.LEG;
                    this.currentClockwareSlotType = slotId == 4 ? ClockwareSlotType.RIGHT_LEG : ClockwareSlotType.LEFT_LEG;
                }
            }

            // Right-click clears the slot
            if (mouseButton == 1) {
                this.mannequin.clockware$setClockwareBySlot(this.currentClockwareSlotType, ItemStack.EMPTY);
                //Sync with server
                PacketDistributor.sendToServer(new SyncClockwareButtonPacket(ItemStack.EMPTY, this.currentClockwareSlotType.getIndex()));
            }

            // Left-click toggles the widget
            if (mouseButton == 0 && (slotId == lastSlotIdClicked || !this.clockwareListWidget.isVisible())) {
                this.clockwareListWidget.toggleVisibility();
            }

            // Play click sound
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WOODEN_TRAPDOOR_OPEN, 1.0F));
        }

        lastSlotIdClicked = slotId;
    }


    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        //Render Price
        int installPrice = this.menu.installPrice;

        if(installPrice>0){
            int initX= this.leftPos+ 150;
            int initY= this.topPos + 225;

            guiGraphics.renderFakeItem(new ItemStack(Items.EMERALD, 1), initX, initY);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 200); // push text way above items

            if(installPrice >1)
                guiGraphics.drawString(this.font, Component.literal(String.valueOf(installPrice)), initX+(installPrice >99? 0: installPrice >9? 5: 11 ), initY+9, 0xffffff, true);
            guiGraphics.pose().popPose();

            int totalEmeralds=0;
            for(int i=0; i < this.playerInventory.items.size(); i++){
                ItemStack itemInSlot = this.playerInventory.items.get(i);

                if(itemInSlot.is(Items.EMERALD)){

                    totalEmeralds=totalEmeralds+itemInSlot.getCount();

                }
            }

            if(totalEmeralds>=installPrice){
                this.installButton.iconName="approve";
                this.installButton.active=true;
            } else {
                this.installButton.iconName="negate";
                this.installButton.active=false;
            }
        } else {
            this.installButton.iconName="none";
            this.installButton.active=false;
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack itemstack = this.hoveredSlot.getItem();

            var tooltip =this.getTooltipFromContainerItem(itemstack);

            if(itemstack.getItem() instanceof ClockwareItem)
                //Replace the "Install with a Ripperdoc" tooltip
                tooltip.replaceAll(c -> {
                    if (Objects.equals(c.getStyle().getInsertion(), "clockware:install_line")
                            && itemstack.getItem() instanceof ClockwareItem clockwareItem) {
                        if(this.menu.ripperdoc==null)
                            return Component.translatable("item.clockware.tooltip.extract").withStyle(ChatFormatting.GRAY);
                        else
                            return Component.translatable("gui.clockware.ripperdoc.price.uninstall",clockwareItem.getInstallPrice()/2).withStyle(ChatFormatting.GREEN);
                    }

                    return c;
                });

            guiGraphics.renderTooltip(this.font, tooltip, itemstack.getTooltipImage(), itemstack, x, y);
        }
    }

    private static void setPlayerMannequin(Minecraft minecraft, GuiGraphics guiGraphics, AbstractClientPlayer mannequin, int x, int y, float partialTick){
        if (minecraft.player != null && minecraft.level!=null) {
            // Point camera straight at the entity from the front
            Quaternionf cameraOrientation = new Quaternionf()
                    .rotateZ((float) Math.toRadians(180))
                    .rotateY((float) Math.toRadians(0));

            // Make entity face forward
            Quaternionf bodyRotation = new Quaternionf()
                    .rotateY((float) Math.toRadians(0))
                    .rotateZ((float) Math.toRadians(0));

            // Lock body/head rotation
            mannequin.yBodyRot = 180.0f;
            mannequin.setYRot(180.0f);
            mannequin.yHeadRot = mannequin.getYRot();
            mannequin.yHeadRotO = mannequin.getYRot();
            mannequin.yBodyRotO = mannequin.getYRot();

            // Animate limbs like vanilla does for idle
//            float ageInTicks = (float) minecraft.level.getGameTime() + partialTick;
//            mannequin.tickCount = (int) ageInTicks;

            //Add it to a team so it won't show the nametag
            if (minecraft.level != null) {
                Scoreboard scoreboard = minecraft.level.getScoreboard();
                PlayerTeam mannequinTeam = scoreboard.getPlayerTeam("mannequin_team");
                if (mannequinTeam == null) {
                    mannequinTeam = scoreboard.addPlayerTeam("mannequin_team");
                    mannequinTeam.setNameTagVisibility(Team.Visibility.NEVER);
                    scoreboard.addPlayerToTeam(mannequin.getGameProfile().getName(), mannequinTeam);
                }
            }

            renderEntityInInventory(
                    guiGraphics,
                    x, y,
                    85,
                    new Vector3f(0, 0, 0),
                    cameraOrientation,
                    bodyRotation,
                    mannequin
            );
        }
    }

    @SuppressWarnings("deprecation")
    public static void renderEntityInInventory(
            GuiGraphics guiGraphics,
            float x,
            float y,
            float scale,
            Vector3f translate,
            Quaternionf pose,
            @Nullable Quaternionf cameraOrientation,
            LivingEntity entity
    ) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 50.0);
        guiGraphics.pose().scale(scale, scale, -scale);
        guiGraphics.pose().translate(translate.x, translate.y, translate.z);
        guiGraphics.pose().mulPose(pose);
        Lighting.setupForFlatItems();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (cameraOrientation != null) {
            entityrenderdispatcher.overrideCameraOrientation(cameraOrientation.conjugate(new Quaternionf()).rotateY((float) Math.PI));
        }

        entityrenderdispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880));
        guiGraphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }
}
