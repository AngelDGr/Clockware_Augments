package mors.clockware.mixin;

import mors.clockware.client.screen.button.InitRipperDocButton;
import mors.clockware.injection.GetVillagerProfession;
import mors.clockware.networking.packets.OpenRipperdocPacket;
import mors.clockware.networking.packets.SyncVillagerProfession;
import mors.clockware.registry.Clockware_Villagers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RipperdocMenuMixins {

    @SuppressWarnings("unused")
    @Mixin(Villager.class)
    private static class VillagerMixin {

        @Unique
        Villager c$this = (Villager)(Object) this;

        @Inject(method = "startTrading", at = @At(value = "TAIL"))
        private void clockware$setProfession(Player player, CallbackInfo ci) {
            //Sync with client
            if(player instanceof ServerPlayer serverPlayer)
                PacketDistributor.sendToPlayer(serverPlayer, new SyncVillagerProfession(c$this.getVillagerData().getProfession().name()));
        }
    }

    @SuppressWarnings("unused")
    @Mixin(MerchantMenu.class)
    private static class MerchantMenuMixin implements GetVillagerProfession {
        @Shadow @Final public Merchant trader;
        @Unique
        private String clockware$profession = "";

        @Override
        public String clockware$getProfession() {
            return clockware$profession;
        }

        @Override
        public void clockware$setProfession(String profession) {
            this.clockware$profession=profession;
        }
    }

    @SuppressWarnings("unused")
    @Mixin(MerchantScreen.class)
    private static abstract class MerchantScreenMixin extends AbstractContainerScreen<MerchantMenu> {

        public MerchantScreenMixin(MerchantMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title);
        }
        @Unique
        private InitRipperDocButton clockware$openRipperdocButton;

        @Inject(method = "init", at = @At("TAIL"))
        private void clockware$setButton(CallbackInfo ci) {
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;

            int k = j + -21;

            //Sync with server
            clockware$openRipperdocButton = this.addRenderableWidget(
                    new InitRipperDocButton(i+105, k,
                            button -> PacketDistributor.sendToServer(new OpenRipperdocPacket())));
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void clockware$setButtonVisible(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
            clockware$openRipperdocButton.visible = this.menu.clockware$getProfession().equals(Clockware_Villagers.RIPPERDOC.get().name());
        }
    }
}
