package mors.clockware.utils;

import mors.clockware.Clockware_Main;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.networking.packets.SyncClockwareItemsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class Clockware_Util {

    @Nullable
    public static ClockwareItem getClockwareItem(ItemStack stack){
        if (stack.getItem() instanceof ClockwareItem clockwareItem) {

            return clockwareItem;
        }

        return null;
    }

    public static<T extends LivingEntity> boolean isSlim(T player){
        if (player instanceof AbstractClientPlayer clientPlayer) {

            return clientPlayer.getSkin().model().equals(PlayerSkin.Model.SLIM);
        }

        return false;
    }

    public static<T extends LivingEntity> String getSlimString(T player){
        if (player instanceof AbstractClientPlayer clientPlayer) {

            return clientPlayer.getSkin().model().equals(PlayerSkin.Model.SLIM)? "slim": "wide";
        }

        return "wide";
    }

    public static  <T extends LivingEntity> ResourceLocation getDefaultGlowClockwareTexture(int level, String slot, String type, T entity){
        return ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID,
                "textures/clockware/" +slot+ "/" +type+ "/"+ level+"_glow.png");
    }
    public static  <T extends LivingEntity> ResourceLocation getDefaultClockwareTexture(int level, String slot, String type, T entity){
        return ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID,
                "textures/clockware/" +slot+ "/" +type+ "/"+ level+".png");
    }

    public static  <T extends LivingEntity> ResourceLocation getArmClockwareTexture(int level, String slot, String type, T entity){
        return ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID,
                "textures/clockware/" +slot+ "/" +type+ "/"+ level+"_" + Clockware_Util.getSlimString(entity)+ ".png");
    }

    public static  <T extends LivingEntity> ResourceLocation getArmGlowClockwareTexture(int level, String slot, String type, T entity){
        return ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID,
                "textures/clockware/" +slot+ "/" +type+ "/"+ level+"_" + Clockware_Util.getSlimString(entity)+ "_glow.png");
    }

    public static int centerTextX(Minecraft minecraft, Component text, int startX){
        // Measure text width
        int textWidth = minecraft.font.width(text);
        return startX - textWidth / 2;
    }

    public static AttributeModifier modifier(String name, double amount, AttributeModifier.Operation operation){
        return new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, name), amount, operation);
    }

    public static AttributeModifier modifierAddition(String name, double amount){
        return modifier(name, amount, AttributeModifier.Operation.ADD_VALUE);
    }

    public static AttributeModifier modifierMultiTotal(String name, double amount){
        return modifier(name, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    public static AttributeModifier modifierMultiBase(String name, double amount){
        return modifier(name, amount, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    /**
     * Use this to sync the player clockware inventory from server to client
     * @param entity The player to sync
     */
    public static void syncClockware(Entity entity){
        if(entity instanceof LivingEntity livingEntity)
            //Sync with client
            PacketDistributor.sendToAllPlayers(
                    new SyncClockwareItemsPacket(livingEntity.clockware$getClockwareInventory(), livingEntity.getId())
            );
    }

    /**
     Simple check to make dual-wielding attacking animations, only when it has both hands empty, an extra condition can be added
     */
    public static boolean hasDualwieldClockware(LivingEntity entity){
        return
                entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
                        && entity.getItemInHand(InteractionHand.OFF_HAND).isEmpty()

                        && entity.clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_ARM).getItem() instanceof DualWieldClockware dualWieldClockware1
                        && entity.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_ARM).getItem() instanceof DualWieldClockware dualWieldClockware2

                        && dualWieldClockware1.extraCondition(entity.clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_ARM))
                        && dualWieldClockware2.extraCondition(entity.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_ARM));
    }



    public static boolean onMainArm(Item item, LivingEntity entity) {
        if(entity.getMainArm().equals(HumanoidArm.LEFT))
            return isClockwareOnSlotSameAs(ClockwareSlotType.LEFT_ARM, item, entity.clockware$getClockwareInventory());

        return isClockwareOnSlotSameAs(ClockwareSlotType.RIGHT_ARM, item, entity.clockware$getClockwareInventory());
    }

    public static boolean onMainArm(String clockware_name, LivingEntity entity) {
        if(entity.getMainArm().equals(HumanoidArm.LEFT))
            return isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_ARM, clockware_name, entity.clockware$getClockwareInventory());

        return isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_ARM, clockware_name, entity.clockware$getClockwareInventory());
    }

    public static boolean onOffArm(Item item, LivingEntity entity) {
        if(entity.getMainArm().equals(HumanoidArm.LEFT))
            return isClockwareOnSlotSameAs(ClockwareSlotType.RIGHT_ARM, item, entity.clockware$getClockwareInventory());

        return isClockwareOnSlotSameAs(ClockwareSlotType.LEFT_ARM, item, entity.clockware$getClockwareInventory());
    }

    public static boolean onOffArm(String clockware_name, LivingEntity entity) {
        if(entity.getMainArm().equals(HumanoidArm.LEFT))
            return isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_ARM, clockware_name, entity.clockware$getClockwareInventory());

        return isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_ARM, clockware_name, entity.clockware$getClockwareInventory());
    }

    public static boolean isClockwareOnSlotSameAsType(ClockwareSlotType slot, String clockware_name, NonNullList<ItemStack> clockwareInventory) {
        return clockwareInventory.get(slot.getIndex()).getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getClockwareName().equals(clockware_name);
    }

    public static boolean isClockwareOnSlotSameAs(ClockwareSlotType slot, Item item, NonNullList<ItemStack> clockwareInventory) {
        return ItemStack.isSameItem(clockwareInventory.get(slot.getIndex()), item.getDefaultInstance());
    }
}
