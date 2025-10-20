package mors.clockware.event;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
    Event triggered when the player does a double jump, used mainly for legs
 */
public class ClockwareDoubleJumpEvent extends ClockwareTickEvent {

    public ClockwareDoubleJumpEvent(LivingEntity entity, NonNullList<ItemStack> clockwareInventory) {
        super(entity, clockwareInventory);
    }
}
