package mors.clockware.item;

import mors.clockware.registry.Clockware_Components;
import mors.clockware.utils.Clockware_Util;
import mors.clockware.utils.DualWieldClockware;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;

public class ReaperBladeArm extends ClockwareItem implements DualWieldClockware {
    public ReaperBladeArm(ClockwareType clockwareType, int level, String clockwareName,  int installPrice) {
        super(new Item.Properties().stacksTo(1)
                        .rarity(level==0? Rarity.COMMON: level==1? Rarity.UNCOMMON: Rarity.RARE)
                        .component(Clockware_Components.BLADE_OUT, false),

                clockwareType, level, clockwareName, installPrice, UseAnim.NONE, "", false);
    }

    @Override
    public boolean doSweep(ItemStack stack, LivingEntity entity, ClockwareSlotType slotType) {
        var bladeOut= stack.get(Clockware_Components.BLADE_OUT);

        if(bladeOut!=null && ItemStack.isSameItemSameComponents(stack, entity.clockware$getClockwareBySlot(slotType)))
            return bladeOut;

        return false;
    }

    @Override
    public boolean extraCondition(ItemStack stack) {
        var bladeOut= stack.get(Clockware_Components.BLADE_OUT);

        if(bladeOut!=null)
            return bladeOut;

        return false;
    }

    public static boolean toggleBlade(LivingEntity entity, SoundEvent pistonSound, ItemStack clockware, boolean value){
        var component = clockware.get(Clockware_Components.BLADE_OUT);

        if(component!=null){

            //To only make the sound if it actually changes
            if(component!=value){
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundSource.PLAYERS,
                        0.2F,
                        0.3F + entity.level().getRandom().nextFloat() * 0.4F);

                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        pistonSound,
                        SoundSource.PLAYERS,
                        0.2F,
                        0.3F);
            }

            clockware.set(Clockware_Components.BLADE_OUT, value);

            if(!entity.level().isClientSide)
                Clockware_Util.syncClockware(entity);

            return true;
        }

        return false;
    }
}
