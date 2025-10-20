package mors.clockware.event;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
    Main event for left-clicking related events, these events DON'T trigger animations and only trigger without looking a block
    @see Start
    @see Release
 */
public class ClockwareLeftClickEvent extends ClockwareTickEvent {

    public ClockwareLeftClickEvent(LivingEntity entity, NonNullList<ItemStack> clockwareInventory) {
        super(entity, clockwareInventory);
    }


    /**
     Event triggered when the player starts left-clicking
     */
    public static class Start extends ClockwareLeftClickEvent {

        public Start(LivingEntity entity, NonNullList<ItemStack> clockwareInventory) {
            super(entity, clockwareInventory);
        }
    }

    /**
     Event triggered when the player stops left-clicking, it returns the amount of time it was left-clicking
     */
    public static class Release extends ClockwareLeftClickEvent {

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
