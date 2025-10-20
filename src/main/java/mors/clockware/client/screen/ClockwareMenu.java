package mors.clockware.client.screen;

import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.registry.Clockware_Screens;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.neoforge.network.IContainerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClockwareMenu extends AbstractContainerMenu {
    @Nullable
    public final Villager ripperdoc;
    public final Player player;
    public int installPrice=0;

    Container clockwareContainers=new SimpleContainer(6){
        @Override
        public void setItem(int slot, @NotNull ItemStack stack) {
            ItemStack old = getItem(slot);
            if (!ItemStack.matches(old, stack)) {
                super.setItem(slot, stack);
                ClockwareMenu.this.slotsChanged(this);
            }
        }
    };

    public final NonNullList<ItemStack> initClockware = NonNullList.withSize(6, ItemStack.EMPTY);

    public ClockwareMenu(int containerId, Inventory inventory, @Nullable Villager ripperdoc) {
        super(Clockware_Screens.CLOCKWARE.get(), containerId);
        this.ripperdoc=ripperdoc;
        this.player=inventory.player;
        //Head
        this.addSlot(new ClockwareSlot(clockwareContainers, 5, 20, 47));
        //Body
        this.addSlot(new ClockwareSlot(clockwareContainers, 2, 196, 47));
        //ArmR
        this.addSlot(new ClockwareSlot(clockwareContainers, 4, 20, 125));
        //ArmL
        this.addSlot(new ClockwareSlot(clockwareContainers, 3, 196, 125));
        //LegR
        this.addSlot(new ClockwareSlot(clockwareContainers, 1, 20, 177));
        //LegL
        this.addSlot(new ClockwareSlot(clockwareContainers, 0, 196, 177));

        //To add them in the exact same order, it gets a final list with all the init clockware, for comparison
        for(int i=0; i<initClockware.size(); i++){
            this.initClockware.set(i, player.clockware$getClockwareInventory().get(i));
        }

        //Sets the clockware on the menu
        for(ClockwareSlotType slotType: ClockwareSlotType.values()){
            this.setClockwareBySlot(slotType, player.clockware$getClockwareBySlot(slotType));
        }
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        if (container != clockwareContainers) return;

        installPrice = 0;
        // Check if any slot differs from the init clockware
        boolean hasChanged = false;
        for (int i = 0; i < clockwareContainers.getContainerSize(); i++) {
            ItemStack toInstall = clockwareContainers.getItem(i);
            ItemStack toModify = initClockware.get(i);
            if (!ItemStack.matches(toInstall, toModify)) {
                hasChanged = true;
                break;
            }
        }

        // Only update the installation price if something changed
        if (hasChanged) {
            for (int i = 0; i < clockwareContainers.getContainerSize(); i++) {
                ItemStack toInstall = clockwareContainers.getItem(i);
                ItemStack toModify = initClockware.get(i);

                //Only triggers if it doesn't match, doesn't detect components
                if(!ItemStack.matches(toInstall, toModify)){
                    // If a new clockware is installed, but it had a clockware before, full price + uninstall price
                    if (!toModify.isEmpty() && toModify.getItem() instanceof ClockwareItem toModifyCw && toInstall.getItem() instanceof ClockwareItem toInstallCw) {
                        installPrice += (toInstallCw.getInstallPrice() + toModifyCw.getInstallPrice()/2);
                    }
                    // If a new clockware is installed, and doesn't have a clockware installed, full price
                    else if (toModify.isEmpty() && toInstall.getItem() instanceof ClockwareItem clockwareItem) {
                        installPrice += clockwareItem.getInstallPrice();
                    }
                    // If original had a clockware but now empty, uninstall price is half
                    else if (toInstall.isEmpty() && toModify.getItem() instanceof ClockwareItem originalItem) {
                        installPrice += originalItem.getInstallPrice() / 2;
                    }
                }
            }
        }


        //Sync without closing the container
        this.broadcastChanges();
        player.inventoryMenu.broadcastChanges();
    }

    @Override
    public boolean clickMenuButton(@NotNull Player player, int id) {
        if (id == 0) {
            int remaining = installPrice;

            //Remove Emeralds
            for (int i = 0; i < player.getInventory().items.size() && remaining > 0; i++) {
                ItemStack stack = player.getInventory().items.get(i);

                if (stack.is(Items.EMERALD)) {
                    int count = stack.getCount();
                    if (count >= remaining) {
                        stack.shrink(remaining);
                        remaining = 0;
                    } else {
                        stack.shrink(count);
                        remaining -= count;
                    }
                }
            }

            // Remove installed items from player's inventory
            for (int i = 0; i < clockwareContainers.getContainerSize(); i++) {
                ItemStack toInstall = clockwareContainers.getItem(i);
                ItemStack toModify = initClockware.get(i);

                //Only triggers if it doesn't match
                if (!ItemStack.isSameItemSameComponents(toInstall, toModify)) {
                    // Remove the new one from inventory
                    if (!toInstall.isEmpty()) {
                        for (int j = 0; j < player.getInventory().items.size(); j++) {
                            ItemStack invStack = player.getInventory().items.get(j);
                            if (ItemStack.isSameItemSameComponents(invStack, toInstall)) {
                                invStack.shrink(1);
                                if (invStack.isEmpty()) {
                                    player.getInventory().items.set(j, ItemStack.EMPTY);
                                }
                                break;
                            }
                        }
                    }

                    // Add the one removed
                    if (!toModify.isEmpty()) {
                        ItemStack removed = toModify.copy();
                        if (!player.getInventory().add(removed)) {
                            // If the inventory is full, drop it near the player
                            player.drop(removed, false);
                        }
                    }
                }
            }

            //Sets the clockware on the PLAYER, FINALLY
            if(player instanceof ServerPlayer serverPlayer){
                //Syncs on the server
                for(ClockwareSlotType slotType: ClockwareSlotType.values()){
                    player.clockware$setClockwareBySlot(slotType, this.getClockwareBySlot(slotType));
                }

                //Sync with client
                Clockware_Util.syncClockware(serverPlayer);
            }

            if(this.ripperdoc!=null){
                ripperdoc.ambientSoundTime = -ripperdoc.getAmbientSoundInterval();
                ripperdoc.playSound(SoundEvents.VILLAGER_YES);
                //Just a placeholder offer to calculate and give XP, the install-price divided by 24
                ripperdoc.rewardTradeXp(new MerchantOffer(new ItemCost(ItemStack.EMPTY.getItem()), ItemStack.EMPTY,12 , (int) Math.min( this.installPrice/24f, 10f), 1f));
                player.awardStat(Stats.TRADED_WITH_VILLAGER);
            }

            //Sync without closing the container
            this.broadcastChanges();
            player.inventoryMenu.broadcastChanges();

            return true;
        }

        return false;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void removed(@NotNull Player player) {
        //This removes the trading player from the villager
        if(ripperdoc!=null){
            ripperdoc.setTradingPlayer(null);
        }
    }

    static class ClockwareSlot extends Slot {

        public ClockwareSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.getItem() instanceof ClockwareItem;
        }

        @Override
        public boolean mayPickup(@NotNull Player player) {
            return false;
        }

        @Override
        public void onTake(@NotNull Player player, @NotNull ItemStack stack) {}
    }

    public void setClockwareBySlot(ClockwareSlotType clockwareSlotType, ItemStack itemStack){
        this.clockwareContainers.setItem(clockwareSlotType.getIndex(), itemStack);
        this.broadcastChanges();
    }

    public ItemStack getClockwareBySlot(ClockwareSlotType slot) {
        return this.clockwareContainers.getItem(slot.getIndex());
    }

    public Container getClockwareContainers() {
        return clockwareContainers;
    }

    public static class Factory implements IContainerFactory<ClockwareMenu> {

        @Override
        public @NotNull ClockwareMenu create(int windowId, @NotNull Inventory inv, @NotNull RegistryFriendlyByteBuf data) {
            boolean hasVillager = data.readBoolean();

            if(hasVillager){
                int entityId = data.readInt(); // Read the villager's ID
                Entity entity = inv.player.level().getEntity(entityId);
                if (entity instanceof Villager villager) {
                    return new ClockwareMenu(windowId, inv, villager);
                }
            }

            return new ClockwareMenu(windowId, inv, null);
        }
    }
}
