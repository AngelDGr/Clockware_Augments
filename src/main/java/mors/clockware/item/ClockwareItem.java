package mors.clockware.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.ItemDescription;
import mors.clockware.client.extensions.ProjectileLauncherClientExtension;
import mors.clockware.item.component.ClockwareUUID;
import mors.clockware.registry.Clockware_Components;
import mors.clockware.utils.Client_Util;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClockwareItem extends Item {
    private final int level;
    private final int installPrice;
    private final ClockwareType clockwareType;
    private final String clockwareName;


    private final boolean usableOnOffHand;
    private final UseAnim useAnim;
    private final String armPose;

    private final boolean hasGlowingEyes;

    public ClockwareItem(Properties properties, ClockwareType clockwareType, int level, String clockwareName, boolean hasSpecificTooltip, int installPrice, UseAnim useAnim, String armPose, boolean usableOnOffHand, boolean hasGlowingEyes) {
        super(properties);
        this.level=level;
        this.installPrice=installPrice;
        this.clockwareType = clockwareType;
        this.clockwareName =clockwareName;
        this.armPose=armPose;
        this.useAnim = useAnim;
        this.usableOnOffHand=usableOnOffHand;
        this.hasGlowingEyes=hasGlowingEyes;

        if(hasSpecificTooltip){
            //Description Key Example -> "item.clockware.[arm].[golem_arm]_[0]"
            ItemDescription.useKey(this, "item.clockware." +this.clockwareType.getName()+ "." + this.clockwareName +"_"+level);
        } else {
            //Description Key Example -> "item.clockware.[arm].[golem_arm]"
            ItemDescription.useKey(this, "item.clockware." +this.clockwareType.getName()+ "." + this.clockwareName);
        }
    }

    public ClockwareItem(ClockwareType clockwareType, int level, String clockwareName, int installPrice, boolean hasSpecificTooltip, boolean hasGlowingEyes) {
        //Default properties
        this(new Item.Properties().stacksTo(1).rarity(level==0? Rarity.COMMON: level==1? Rarity.UNCOMMON: Rarity.RARE),
                clockwareType, level, clockwareName, hasSpecificTooltip, installPrice, UseAnim.NONE, "EMPTY", false, hasGlowingEyes);
    }

    public ClockwareItem(ClockwareType clockwareType, int level, String clockwareName, int installPrice, boolean hasSpecificTooltip) {
        //Default properties
        this(new Item.Properties().stacksTo(1).rarity(level==0? Rarity.COMMON: level==1? Rarity.UNCOMMON: Rarity.RARE),
                clockwareType, level, clockwareName, hasSpecificTooltip, installPrice, UseAnim.NONE, "EMPTY", false, false);
    }

    public ClockwareItem(ClockwareType clockwareType, int level, String clockwareName, int installPrice, Boolean hasGlowingEyes) {
        //Default properties
        this(new Item.Properties().stacksTo(1).rarity(level==0? Rarity.COMMON: level==1? Rarity.UNCOMMON: Rarity.RARE),
                clockwareType, level, clockwareName, false, installPrice, UseAnim.NONE, "EMPTY", false, hasGlowingEyes);
    }

    public ClockwareItem(ClockwareType clockwareType, int level, String clockwareName, int installPrice) {
        //Default properties
        this(new Item.Properties().stacksTo(1).rarity(level==0? Rarity.COMMON: level==1? Rarity.UNCOMMON: Rarity.RARE),
                clockwareType, level, clockwareName, false, installPrice, UseAnim.NONE, "EMPTY", false, false);
    }

    public ClockwareItem(Properties properties, ClockwareType clockwareType, int level, String clockwareName, int installPrice, UseAnim useAnim, String armPose, boolean usableOnOffHand) {
        //Default properties
        this(properties,
                clockwareType, level, clockwareName, false, installPrice, useAnim, armPose, usableOnOffHand, false);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide()) {
            ensureHasUUID(stack);
        }
    }

    public static void ensureHasUUID(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        var type = Clockware_Components.CLOCKWARE_UUID.get();
        if (!stack.has(type)) {
            stack.set(type, new ClockwareUUID(UUID.randomUUID()));
        }
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.set(Clockware_Components.CLOCKWARE_UUID.get(), new ClockwareUUID(UUID.randomUUID()));
        return stack;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);


        var description = ItemDescription.create(this, FontHelper.Palette.STANDARD_CREATE);

        if(description!=null){


            if (Client_Util.isShiftDown()) {
                //First line ("Press shift for summary")
                var firstLine= description.linesOnShift().getFirst();
                tooltipComponents.add(firstLine);

                tooltipComponents.add(Component.empty());

                tooltipComponents.add(Component.translatable("item.clockware.tooltip.type_"+ clockwareType.getName()).withStyle(FontHelper.Palette.STANDARD_CREATE.highlight()));
                tooltipComponents.add(Component.translatable("item.clockware.tooltip.level_"+level).withStyle((level==0? ChatFormatting.GRAY: level==1? ChatFormatting.DARK_GREEN: ChatFormatting.GOLD)));

                //Rest of the description
                List<Component> shiftLines = new ArrayList<>(description.linesOnShift());
                shiftLines.removeFirst();

                tooltipComponents.addAll(shiftLines);

                tooltipComponents.add(Component.empty());
                //Uses insertion to replace it on the installing screen
                tooltipComponents.add(Component.translatable("item.clockware.tooltip.install").withStyle((ChatFormatting.GRAY)).withStyle(style -> style.withInsertion("clockware:install_line")));

            } else if (Client_Util.isCtrlDown()) {
                tooltipComponents.addAll(description.linesOnCtrl());
            } else {
                tooltipComponents.addAll(description.lines());
            }
        }
    }

    public String getClockwareName() {
        return clockwareName;
    }

    public int getLevel() {
        return level;
    }

    public int getInstallPrice() {
        return installPrice;
    }

    public ClockwareType getType() {
        return clockwareType;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable("item.clockware."+clockwareName);
    }

    /**
     * If attacking with this item equipped should trigger a sword sweep
     * @param stack The stack of the clockware
     * @param entity The entity to check
     * @param slotType To being able to trigger depending on the installed slot
     */
    public boolean doSweep(ItemStack stack, LivingEntity entity, ClockwareSlotType slotType){
        return false;
    }

    /**
     * Return any pose other than {@linkplain HumanoidModel.ArmPose#EMPTY} to animate on third person while right-clicking, assigned on constructor or override as the enum string:
     * EMPTY, ITEM, BLOCK, etc.
     * <p>
     * Can use {@linkplain IClientItemExtensions#getArmPose(LivingEntity, InteractionHand, ItemStack)} instead, see {@linkplain ProjectileLauncherClientExtension} for example
     */
    public String getArmPose(){
        return this.armPose;
    }

    /**
     * Return any animation other than {@linkplain UseAnim#NONE} to animate on first person while right-clicking, assigned on constructor or override
     * <p>
     * Can use {@linkplain IClientItemExtensions#applyForgeHandTransform(PoseStack, LocalPlayer, HumanoidArm, ItemStack, float, float, float)} instead, see {@linkplain ProjectileLauncherClientExtension} for example
     */
    public @NotNull UseAnim getUseAnimation() {
        return this.useAnim;
    }

    /**
     Return false to no animate if it is on offhand, assigned on constructor or override
     */
    public boolean isUsableOnOffHand() {
        return usableOnOffHand;
    }

    /**
     If true, it tries to render a [level]_glow.png texture while rendering that glows like spider eyes
     */
    public boolean hasGlowingMask() {
        return hasGlowingEyes;
    }
}
