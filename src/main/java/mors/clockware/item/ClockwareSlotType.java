package mors.clockware.item;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public enum ClockwareSlotType {
    LEFT_LEG("left_leg", 0),
    RIGHT_LEG("right_leg", 1),
    BODY("body", 2),
    LEFT_ARM("left_arm", 3),
    RIGHT_ARM("right_arm", 4),
    HEAD("head", 5);

    private final String name;
    private final int index;
    ClockwareSlotType(String name, int index){
        this.name=name;
        this.index=index;
    }
    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public static ClockwareSlotType getMainArmSlot(LivingEntity entity) {
        return entity.getMainArm().equals(HumanoidArm.RIGHT)? ClockwareSlotType.RIGHT_ARM: ClockwareSlotType.LEFT_ARM;
    }

    public static ClockwareSlotType getOffArmSlot(LivingEntity entity) {
        return entity.getMainArm().equals(HumanoidArm.RIGHT)? ClockwareSlotType.LEFT_ARM: ClockwareSlotType.RIGHT_ARM;
    }
}
