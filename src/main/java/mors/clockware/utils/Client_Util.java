package mors.clockware.utils;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Client_Util {

    public static boolean isShiftDown() {
        return Screen.hasShiftDown();
    }
    public static boolean isCtrlDown() {
        return Screen.hasControlDown();
    }


    public static HumanoidModel.ArmPose getArmPoseFromString(String name) {
        try {
            return HumanoidModel.ArmPose.valueOf(name);
        } catch (IllegalArgumentException e) {
            return HumanoidModel.ArmPose.EMPTY;
        }
    }
}