package mors.clockware.mixin.client;

import com.mojang.authlib.GameProfile;
import mors.clockware.networking.packets.buttons.DoubleJumpClockware;
import mors.clockware.networking.packets.buttons.StartClockwareClick;
import mors.clockware.networking.packets.buttons.StopClockwareRightClick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow @Final protected Minecraft minecraft;

    @Shadow public Input input;

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) { super(clientLevel, gameProfile);}

    @Unique
    private boolean c$wasLeftClicking = false;
    @Unique
    private int c$LeftUseTime = 0;

    @Unique
    private boolean c$wasRightClicking = false;

    @Unique
    protected int c$jumpTriggerTimeClockware;
    @Unique
    private boolean c$wasJumpingClockware;
    @Unique
    private boolean c$hasDoubleJumpedClockware;

    @Unique
    LocalPlayer c$this = (LocalPlayer)(Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    private void clockware$detectKeysForActions(CallbackInfo ci) {

        //Left Click
        {
            boolean isLeftClicking = this.minecraft.options.keyAttack.isDown();

            HitResult hit = Minecraft.getInstance().hitResult;
            boolean miningBlock = hit != null && hit.getType() == HitResult.Type.BLOCK;

            if (isLeftClicking && !miningBlock) {
                if (!c$wasLeftClicking) {
                    //Triggers when starting the left click
                    PacketDistributor.sendToServer(new StartClockwareClick(false, c$this.getId()));
                }

                c$LeftUseTime++;
            }

            if ((!isLeftClicking && c$wasLeftClicking) || miningBlock) {
                // Player just released left click, restart the useTime too
                if (c$LeftUseTime > 0)
                    PacketDistributor.sendToServer(new StopClockwareRightClick(c$LeftUseTime, false, c$this.getId()));

                c$LeftUseTime = 0;
            }

            c$wasLeftClicking = isLeftClicking;
        }

        //Right Click
        {
            boolean isRightClicking = this.minecraft.options.keyUse.isDown();

            if (isRightClicking && !c$this.isUsingItem()) {
                if (!c$wasRightClicking) {
                    //Triggers when starting the right click
                    PacketDistributor.sendToServer(new StartClockwareClick(true, c$this.getId()));
                }


                c$this.clockware$setRightUseTime(c$this.clockware$getRightUseTime()+1);
            }

            if (!isRightClicking && c$wasRightClicking) {
                // Player just released right click, restart the useTime too
                if (c$this.clockware$getRightUseTime() > 0)
                    PacketDistributor.sendToServer(new StopClockwareRightClick(c$this.clockware$getRightUseTime(), true, c$this.getId()));

                c$this.clockware$setRightUseTime(0);
            }

            c$wasRightClicking = isRightClicking;
        }

        //Jump
        {
            if (this.c$jumpTriggerTimeClockware > 0) {
                this.c$jumpTriggerTimeClockware--;
            }

            boolean justPressedJump = this.input.jumping && !this.c$wasJumpingClockware;

            //Double tap on ground
            if (justPressedJump) {
                if (this.c$jumpTriggerTimeClockware == 0) {
                    this.c$jumpTriggerTimeClockware = 7;
                } else {
                    PacketDistributor.sendToServer(new DoubleJumpClockware(c$this.getId()));
                    this.c$jumpTriggerTimeClockware = 0;
                    this.c$hasDoubleJumpedClockware = true;
                }
            }

            // Midair (falling) double-jump trigger
            if (justPressedJump && !c$this.onGround() && !this.c$hasDoubleJumpedClockware) {
                // Check for downward motion to ensure the player is falling
                if (c$this.getDeltaMovement().y < 0.0D) {
                    PacketDistributor.sendToServer(new DoubleJumpClockware(c$this.getId()));
                    this.c$hasDoubleJumpedClockware = true;
                }
            }

            // Reset midair flag when player lands
            if (c$this.onGround()) {
                this.c$hasDoubleJumpedClockware = false;
            }

            this.c$wasJumpingClockware = this.input.jumping;
        }
    }

    @Unique
    private int c$RightUseTime = 0;

    @Override
    public boolean clockware$isRightClicking() {
        return c$RightUseTime>2;
    }

    @Override
    public int clockware$getRightUseTime() {
        return c$RightUseTime;
    }

    @Override
    public void clockware$setRightUseTime(int useTime) {
        c$RightUseTime=useTime;
    }
}
