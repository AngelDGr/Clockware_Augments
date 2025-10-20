package mors.clockware.event;

import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
/**
    Event triggered on clockware compatibilities entities each tick, used for applying attributes or doing other tick-related systems
 */
public class ClockwareTickEvent extends Event {
    private final LivingEntity entity;
    private final NonNullList<ItemStack> clockwareInventory;

	public ClockwareTickEvent(LivingEntity entity, NonNullList<ItemStack> clockwareInventory) {
		this.entity = entity;
        this.clockwareInventory=clockwareInventory;
	}

    public LivingEntity getEntity() {
        return entity;
    }

    public NonNullList<ItemStack> getClockwareInventory() {
        return clockwareInventory;
    }

    public ItemStack getClockwareBySlot(ClockwareSlotType slot) {
        return clockwareInventory.get(slot.getIndex());
    }

    public ItemStack clockwareOnMainArm() {
        if(this.entity.getMainArm().equals(HumanoidArm.LEFT))
            return getClockwareBySlot(ClockwareSlotType.LEFT_ARM);

        return getClockwareBySlot(ClockwareSlotType.RIGHT_ARM);
    }

    public ItemStack clockwareOnOffArm() {
        if(this.entity.getMainArm().equals(HumanoidArm.RIGHT))
            return getClockwareBySlot(ClockwareSlotType.LEFT_ARM);

        return getClockwareBySlot(ClockwareSlotType.RIGHT_ARM);
    }

    /**
        Returns the clockware level on a specific {@linkplain ClockwareSlotType}
     @param slot The slot to check
     @return The level, -1 if the item is not a {@linkplain ClockwareItem}
     */
    public int getClockwareLevelBySlot(ClockwareSlotType slot) {
        ItemStack clockware= clockwareInventory.get(slot.getIndex());

        if(clockware.getItem() instanceof ClockwareItem clockwareItem)
            return clockwareItem.getLevel();

        return -1;
    }

    //Attributes
    public void setAttribute(Holder<Attribute> attributeType, AttributeModifier modifier){
        AttributeInstance instance = this.entity.getAttribute(attributeType);

        if(instance!=null){
            if(!instance.hasModifier(modifier.id())){
                instance.addPermanentModifier(modifier);
            } else if(instance.hasModifier(modifier.id())) {
                instance.removeModifier(modifier);
            }
        }
    }

    public void setAttribute_RightLeg_LeftLeg(Item item, Holder<Attribute> attributeType, AttributeModifier modifierRightLeg, AttributeModifier modifierLeftLeg){
        //Right Leg modifier
        this.setAttributeIf(
                this.onRightLeg(item),
                attributeType,
                modifierRightLeg
        );

        //Left Leg modifier
        this.setAttributeIf(
                this.onLeftLeg(item),
                attributeType,
                modifierLeftLeg
        );
    }

    public void setAttribute_OffHand_MainHand(Item item, Holder<Attribute> attributeType, AttributeModifier modifierMainHand, AttributeModifier modifierOffHand){
        //MainHand modifier
        this.setAttributeIf(
                this.onMainArm(item),
                attributeType,
                modifierMainHand
        );

        //OffHand modifier
        this.setAttributeIf(
                this.onOffArm(item),
                attributeType,
                modifierOffHand
        );
    }

    public void setAttributeIf(boolean condition, Holder<Attribute> attributeType, AttributeModifier modifier){
        AttributeInstance instance = this.entity.getAttribute(attributeType);

        if(instance!=null){
            if(condition && !instance.hasModifier(modifier.id())){
                instance.addPermanentModifier(modifier);
            } else if(!condition && instance.hasModifier(modifier.id())) {
                instance.removeModifier(modifier);
            }
        }
    }

    //Slot Helpers
    public boolean isClockwareOnSlotSameAsType(ClockwareSlotType slot, String clockware_name) {
        return clockwareInventory.get(slot.getIndex()).getItem() instanceof ClockwareItem clockwareItem && clockwareItem.getClockwareName().equals(clockware_name);
    }

    public boolean isClockwareOnSlotSameAs(ClockwareSlotType slot, Item item) {
        return ItemStack.isSameItem(clockwareInventory.get(slot.getIndex()), item.getDefaultInstance());
    }

    // ---- Head ----
    public boolean onHead(Item item) {
        return isClockwareOnSlotSameAs(ClockwareSlotType.HEAD, item);
    }

    public boolean onHead(String clockware_name) {
        return isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, clockware_name);
    }

    // ---- Body ----
    public boolean onBody(Item item) {
        return isClockwareOnSlotSameAs(ClockwareSlotType.BODY, item);
    }

    public boolean onBody(String clockware_name) {
        return isClockwareOnSlotSameAsType(ClockwareSlotType.BODY, clockware_name);
    }

    // ---- Arms ----
    public boolean onMainArm(Item item) {
        if(this.entity.getMainArm().equals(HumanoidArm.LEFT))
            return isClockwareOnSlotSameAs(ClockwareSlotType.LEFT_ARM, item);

        return isClockwareOnSlotSameAs(ClockwareSlotType.RIGHT_ARM, item);
    }

    public boolean onMainArm(String clockware_name) {
        if(this.entity.getMainArm().equals(HumanoidArm.LEFT))
            return isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_ARM, clockware_name);

        return isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_ARM, clockware_name);
    }

    public boolean onOffArm(Item item) {
        if(this.entity.getMainArm().equals(HumanoidArm.LEFT))
            return isClockwareOnSlotSameAs(ClockwareSlotType.RIGHT_ARM, item);

        return isClockwareOnSlotSameAs(ClockwareSlotType.LEFT_ARM, item);
    }

    public boolean onOffArm(String clockware_name) {
        if(this.entity.getMainArm().equals(HumanoidArm.LEFT))
            return isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_ARM, clockware_name);

        return isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_ARM, clockware_name);
    }

    public boolean onLeftArm(Item item) {
        return isClockwareOnSlotSameAs(ClockwareSlotType.LEFT_ARM, item);
    }

    public boolean onLeftArm(String clockware_name) {
        return isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_ARM, clockware_name);
    }

    public boolean onRightArm(Item item) {
        return isClockwareOnSlotSameAs(ClockwareSlotType.RIGHT_ARM, item);
    }

    public boolean onRightArm(String clockware_name) {
        return isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_ARM, clockware_name);
    }

    public boolean onAnyArm(Item item) {
        return onLeftArm(item) || onRightArm(item);
    }

    public boolean onAnyArm(String clockware_name) {
        return onLeftArm(clockware_name) || onRightArm(clockware_name);
    }

    // ---- Legs ----
    public boolean onLeftLeg(Item item) {
        return isClockwareOnSlotSameAs(ClockwareSlotType.LEFT_LEG, item);
    }

    public boolean onLeftLeg(String clockware_name) {
        return isClockwareOnSlotSameAsType(ClockwareSlotType.LEFT_LEG, clockware_name);
    }

    public boolean onRightLeg(Item item) {
        return isClockwareOnSlotSameAs(ClockwareSlotType.RIGHT_LEG, item);
    }

    public boolean onRightLeg(String clockware_name) {
        return isClockwareOnSlotSameAsType(ClockwareSlotType.RIGHT_LEG, clockware_name);
    }

    public boolean onAnyLeg(Item item) {
        return onLeftLeg(item) || onRightLeg(item);
    }

    public boolean onAnyLeg(String clockware_name) {
        return onLeftLeg(clockware_name) || onRightLeg(clockware_name);
    }

    public boolean onBothLegs(Item item) {
        return onLeftLeg(item) && onRightLeg(item);
    }

    public boolean onBothLegs(String clockware_name) {
        return onLeftLeg(clockware_name) && onRightLeg(clockware_name);
    }
}
