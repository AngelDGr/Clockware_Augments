package mors.clockware.event;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
    Main event for right-clicking related events
    @see Start
    @see Release
 */
public class ClockwareRightClickEvent extends ClockwareTickEvent {

    public ClockwareRightClickEvent(LivingEntity entity, NonNullList<ItemStack> clockwareInventory) {
        super(entity, clockwareInventory);
    }


    /**
     Event triggered when the player starts right-clicking
     */
    public static class Start extends ClockwareRightClickEvent{

        public Start(LivingEntity entity, NonNullList<ItemStack> clockwareInventory) {
            super(entity, clockwareInventory);
        }
    }

    /**
     Event triggered when the player stops right-clicking, it returns the amount of time it was right-clicking
     */
    public static class Release extends ClockwareRightClickEvent{

        private final int useTime;

        public Release(LivingEntity entity, NonNullList<ItemStack> clockwareInventory, int useTime) {
            super(entity, clockwareInventory);
            this.useTime=useTime;
        }

        public int getUseTime() {
            return useTime;
        }
    }
}
