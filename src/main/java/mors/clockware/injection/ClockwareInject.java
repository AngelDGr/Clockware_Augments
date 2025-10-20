package mors.clockware.injection;

import mors.clockware.item.ClockwareSlotType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface ClockwareInject {

    default NonNullList<ItemStack> clockware$getClockwareInventory(){return null;}

    default ItemStack clockware$getClockwareBySlot(ClockwareSlotType slot){return null;}

    default ItemStack clockware$getClockwareOnMainHand(){return null;}

    default ItemStack clockware$getClockwareOnOffHand(){return null;}

    default void clockware$setClockwareBySlot(ClockwareSlotType slot, ItemStack stack){}

    default boolean clockware$canEntityTriggerClockwareTick(){return false;};

}
