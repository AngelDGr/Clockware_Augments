package mors.clockware.mixin.clockware;

import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

public class ArachnidOpticsMixins {

    @Mixin(LightTexture.class)
    public abstract static class LightTextureMixin {

        @Shadow @Final private Minecraft minecraft;

        @ModifyVariable(method = "updateLightTexture", at = @At("STORE"), ordinal = 7)
        private float clockware$extraArachnidLight(float value){

            if(this.minecraft.player!=null)

                if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "arachnid_optics", this.minecraft.player.clockware$getClockwareInventory())){

                    ItemStack clockwareOnHead= this.minecraft.player.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

                    if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem) {

                        //Vanilla   ->
                        //Prototype -> 0.25
                        //Refined   -> 0.50
                        //Masterwork-> 0.75

                        return Math.min(value + (0.25f + (clockwareItem.getLevel()*0.25f)), 1.0f);
                    }
                }

            return value;
        }
    }

    @Mixin(Player.class)
    public abstract static class PlayerMixin {

        @Unique
        public final Player c$this = (Player) (Object) this;

        @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5f))
        private float clockware$extraArachnidCrit(final float value){
            if(Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "arachnid_optics", c$this.clockware$getClockwareInventory())){

                ItemStack clockwareOnHead= c$this.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);
                if(clockwareOnHead.getItem() instanceof ClockwareItem clockwareItem){

                    //Vanilla   -> 1.50
                    //Prototype -> 1.75
                    //Refined   -> 2.00
                    //Masterwork-> 2.25
                    return value + (0.25f + (clockwareItem.getLevel()*0.25f));
                }
            }

            return value;
        }
    }

}
