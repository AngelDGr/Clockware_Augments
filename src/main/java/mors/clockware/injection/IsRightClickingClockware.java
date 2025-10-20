package mors.clockware.injection;

public interface IsRightClickingClockware {

    default void clockware$setIsRightClicking(boolean isRightClicking){}

    default boolean clockware$isRightClicking(){return false;}

    default int clockware$getRightUseTime(){return 0;}

    default void clockware$setRightUseTime(int useTime){}

}
