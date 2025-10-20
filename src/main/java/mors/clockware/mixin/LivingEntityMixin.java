package mors.clockware.mixin;

import mors.clockware.event.ClockwareTickEvent;
import mors.clockware.injection.ClockwareInject;
import mors.clockware.item.ClockwareSlotType;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ILivingEntityExtension, ClockwareInject {
    @Shadow protected abstract void verifyEquippedItem(ItemStack stack);

    @Unique
    public final NonNullList<ItemStack> clockware$clockwareInventory = NonNullList.withSize(6, ItemStack.EMPTY);

    @Unique
    public final LivingEntity c$this = (LivingEntity) (Object) this;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void clockware$addClockwareSlots(CompoundTag compound, CallbackInfo ci) {
        ListTag listtag = new ListTag();
        for (ItemStack itemstack : this.clockware$clockwareInventory) {
            if (!itemstack.isEmpty()) {
                listtag.add(itemstack.save(this.registryAccess()));
            } else {
                listtag.add(new CompoundTag());
            }
        }

        compound.put("ClockwareItems", listtag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void clockware$readClockwareSlots(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("ClockwareItems", 9)) {
            ListTag listtag = compound.getList("ClockwareItems", 10);

            for (int i = 0; i < this.clockware$clockwareInventory.size(); i++) {
                CompoundTag compoundtag = listtag.getCompound(i);
                this.clockware$clockwareInventory.set(i, ItemStack.parseOptional(this.registryAccess(), compoundtag));
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void clockware$tickEvent(CallbackInfo ci) {
        //Only fires if the event for players, zombies and skeletons, do a mixin into a class to add other ones
        if(this.clockware$canEntityTriggerClockwareTick())
            NeoForge.EVENT_BUS.post(new ClockwareTickEvent(c$this, this.clockware$getClockwareInventory()));
    }

    @Override
    public ItemStack clockware$getClockwareBySlot(ClockwareSlotType slot) {
        return this.clockware$clockwareInventory.get(slot.getIndex());
    }

    @Override
    public ItemStack clockware$getClockwareOnMainHand() {
        if(c$this.getMainArm().equals(HumanoidArm.LEFT))
            return clockware$getClockwareBySlot(ClockwareSlotType.LEFT_ARM);

        return clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_ARM);
    }

    @Override
    public ItemStack clockware$getClockwareOnOffHand() {
        if(c$this.getMainArm().equals(HumanoidArm.LEFT))
            return clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_ARM);

        return clockware$getClockwareBySlot(ClockwareSlotType.LEFT_ARM);
    }

    @Override
    public void clockware$setClockwareBySlot(ClockwareSlotType slot, ItemStack stack) {
        this.verifyEquippedItem(stack);
        this.clockware$clockwareInventory.set(slot.getIndex(), stack);
    }

    @Override
    public NonNullList<ItemStack> clockware$getClockwareInventory() {
        return this.clockware$clockwareInventory;
    }

    @Override
    public boolean clockware$canEntityTriggerClockwareTick() {
        return c$this instanceof Player || c$this instanceof Zombie || c$this instanceof AbstractSkeleton;
    }
}
