package mors.clockware.utils;

import net.minecraft.world.item.ItemStack;

/**
 * Simple utility class to animate clockware with dual-wielding attacking animations
 */
public interface DualWieldClockware {

    /**
     * Extra condition to being able to do the dual-wielding
     */
    default boolean extraCondition(ItemStack stack){
        return true;
    }

    /**
     * To show the offhand on first-person if it is dual-wielding
     */
    default boolean showOffHand(){
        return true;
    }
}
