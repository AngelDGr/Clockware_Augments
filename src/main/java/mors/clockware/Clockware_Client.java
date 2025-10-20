package mors.clockware;

import mors.clockware.client.ProjectileArmStorageClient;
import mors.clockware.client.extensions.ProjectileLauncherClientExtension;
import mors.clockware.client.layer.*;
import mors.clockware.client.model.*;
import mors.clockware.client.screen.ClockwareScreen;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.item.ProjectileLauncherArm;
import mors.clockware.item.component.ProjectileArmStorage;
import mors.clockware.registry.Clockware_Components;
import mors.clockware.registry.Clockware_Items;
import mors.clockware.registry.Clockware_Screens;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Clockware_Main.MOD_ID, value = Dist.CLIENT)
@Mod(value = Clockware_Main.MOD_ID, dist = Dist.CLIENT)
public class Clockware_Client {

    public static ModelLayerLocation CLOCKWARE_HEAD_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_head"), "clockware_head");

    public static ModelLayerLocation CLOCKWARE_BODY_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_body"), "clockware_body");

    public static ModelLayerLocation CLOCKWARE_RIGHT_ARM_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_right_arm"), "clockware_right_arm");
    public static ModelLayerLocation CLOCKWARE_LEFT_ARM_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_left_arm"), "clockware_left_arm");

    public static ModelLayerLocation CLOCKWARE_RIGHT_LEG_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_right_leg"), "clockware_right_leg");
    public static ModelLayerLocation CLOCKWARE_LEFT_LEG_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "clockware_left_leg"), "clockware_left_leg");



    @SubscribeEvent
    public static void registerItemClientExtensions(final RegisterClientExtensionsEvent event) {
        event.registerItem(new ProjectileLauncherClientExtension(),
                Clockware_Items.PROJECTILE_LAUNCHER_PROTOTYPE,
                Clockware_Items.PROJECTILE_LAUNCHER_REFINED,
                Clockware_Items.PROJECTILE_LAUNCHER_MASTERWORK);
    }

    @SubscribeEvent
    public static void addGUIForLauncher(final RenderGuiLayerEvent.Pre event) {

        if (event.getName().equals(VanillaGuiLayers.HOTBAR)){

            Minecraft mc = Minecraft.getInstance();

            int guiWidth = mc.getWindow().getGuiScaledWidth();
            int guiHeight = mc.getWindow().getGuiScaledHeight();

            GuiGraphics guiGraphics = event.getGuiGraphics();

            int startY =  guiHeight-18;

            int startXRight =  guiWidth-18;

            Player player = mc.player;

            renderProjectileLauncherGUI(guiGraphics, player, HumanoidArm.RIGHT, startXRight, startY);

            int startXLeft =  0;

            renderProjectileLauncherGUI(guiGraphics, player, HumanoidArm.LEFT, startXLeft, startY);

        }
    }

    public static void renderProjectileLauncherGUI(GuiGraphics guiGraphics, Player player, HumanoidArm arm, int startX, int startY){
        if(Minecraft.getInstance().player!=null && Minecraft.getInstance().player.clockware$isRightClicking()){

            boolean isRight= arm.equals(HumanoidArm.RIGHT);

            ClockwareSlotType clockwareSlotType = isRight? ClockwareSlotType.RIGHT_ARM: ClockwareSlotType.LEFT_ARM;

            if(player.clockware$getClockwareBySlot(clockwareSlotType).getItem() instanceof ProjectileLauncherArm &&
                    player.getItemInHand(player.getMainArm().equals(arm)? InteractionHand.MAIN_HAND:  InteractionHand.OFF_HAND).isEmpty()){

                ItemStack launcherStack = player.clockware$getClockwareBySlot(clockwareSlotType);

                ProjectileArmStorage storage = launcherStack.get(Clockware_Components.PROJECTILE_ARM_STORAGE);
                if(storage!=null){

                    if(!storage.items().isEmpty()){
                        int slotX = storage.items().size()>1? startX+ ((isRight?-1: 1) * 18) :startX ;

                        guiGraphics.blit(ProjectileArmStorageClient.SLOT,
                                slotX-1, startY -1,
                                0, 0,
                                20, 20,
                                20, 20);

                        guiGraphics.renderItem(storage.items().getFirst(), slotX, startY);

                        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, storage.items().getFirst(), slotX, startY);
                    } if (storage.items().size()>1) {

                        guiGraphics.pose().pushPose();
                        guiGraphics.pose().translate(0, 0, 500);
                        guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "icon/"+(arm.equals(HumanoidArm.RIGHT)? "next":  "previous")),
                                18, 18,
                                0, 0,
                                startX, startY,
                                18, 18);
                        guiGraphics.pose().popPose();


                        guiGraphics.blit(ProjectileArmStorageClient.SLOT,
                                startX -1, startY -1,
                                0, 0,
                                20, 20,
                                20, 20);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void modifyFovEvent(final ComputeFovModifierEvent event) {
        if(event.getPlayer() instanceof LocalPlayer localPlayer){

            int useTime = localPlayer.clockware$getRightUseTime();
            ItemStack clockwareOnRight = localPlayer.clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_ARM);
            ItemStack clockwareOnLeft  =  localPlayer.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_ARM);

            //Projectile Launcher Shoot Fov
            if(localPlayer.clockware$isRightClicking() && !localPlayer.isUsingItem()){

                if(clockwareOnRight.getItem() instanceof ProjectileLauncherArm projectileLauncherArm && localPlayer.getItemInHand(localPlayer.getMainArm().equals(HumanoidArm.RIGHT)? InteractionHand.MAIN_HAND: InteractionHand.OFF_HAND).isEmpty()){
                    float newFov = calculateNewFov(event, projectileLauncherArm, (float) useTime);

                    event.setNewFovModifier(newFov);
                }
                else if(clockwareOnLeft.getItem() instanceof ProjectileLauncherArm projectileLauncherArm && localPlayer.getItemInHand(localPlayer.getMainArm().equals(HumanoidArm.LEFT)? InteractionHand.MAIN_HAND: InteractionHand.OFF_HAND).isEmpty()){
                    float newFov = calculateNewFov(event, projectileLauncherArm, (float) useTime);

                    event.setNewFovModifier(newFov);
                }
            }

            //Reaper Blade Lunge Fov
            if(Clockware_Util.hasDualwieldClockware(localPlayer) && Clockware_Util.onMainArm("reaper_blade", localPlayer) && Clockware_Util.onOffArm("reaper_blade", localPlayer)
             && !localPlayer.getCooldowns().isOnCooldown(localPlayer.clockware$getClockwareOnMainHand().getItem()) && localPlayer.getFoodData().getFoodLevel()>2){
                float maxDuration = 10;

                float newFov;
                float f1 = useTime / maxDuration;
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                } else {
                    f1 *= f1;
                }


                newFov = event.getFovModifier() * (1.0F - f1 * 0.2F);
                event.setNewFovModifier(newFov);
            }
        }
    }

    private static float calculateNewFov(ComputeFovModifierEvent event, ProjectileLauncherArm projectileLauncherArm, float useTime) {
        int clockwareLevel = projectileLauncherArm.getLevel();
        float maxDuration = clockwareLevel==0? 40: clockwareLevel==1? 30: 20;

        float newFov;
        float f1 = useTime / maxDuration;
        if (f1 > 1.0F) {
            f1 = 1.0F;
        } else {
            f1 *= f1;
        }


        newFov = event.getFovModifier() * (1.0F - f1 * (clockwareLevel ==0? 0.10F: clockwareLevel ==1? 0.15F: 0.25f));
        return newFov;
    }

    @SubscribeEvent
    public static void registerTooltipComponentsRender(final RegisterClientTooltipComponentFactoriesEvent event){
        event.register(ProjectileArmStorage.class, ProjectileArmStorageClient::new);
    }
    @SubscribeEvent
    public static void registerClientEvent(final FMLClientSetupEvent event){}


    @SubscribeEvent
    public static void registerEntityLayers(final EntityRenderersEvent.AddLayers event) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance()
                .getEntityRenderDispatcher();

        //Clockware
        ClockwareHeadLayer.registerOnAll(dispatcher, event.getContext());

        ClockwareRightArmLayer.registerOnAll(dispatcher, event.getContext());
        ClockwareLeftArmLayer.registerOnAll(dispatcher, event.getContext());

        ClockwareBodyLayer.registerOnAll(dispatcher, event.getContext());

        ClockwareRightLegLayer.registerOnAll(dispatcher, event.getContext());
        ClockwareLeftLegLayer.registerOnAll(dispatcher, event.getContext());

        //Misc
        OpticalCalibratorOutlineLayer.registerOnAll(dispatcher);
    }



    @SubscribeEvent
    public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CLOCKWARE_HEAD_LAYER, ClockwareHeadModel::createLayer);

        event.registerLayerDefinition(CLOCKWARE_BODY_LAYER, ClockwareBodyModel::createLayer);

        event.registerLayerDefinition(CLOCKWARE_RIGHT_ARM_LAYER, ClockwareRightArmModel::createLayer);
        event.registerLayerDefinition(CLOCKWARE_LEFT_ARM_LAYER, ClockwareLeftArmModel::createLayer);

        event.registerLayerDefinition(CLOCKWARE_RIGHT_LEG_LAYER, ClockwareRightLegModel::createLayer);
        event.registerLayerDefinition(CLOCKWARE_LEFT_LEG_LAYER, ClockwareLeftLegModel::createLayer);
    }

    @SubscribeEvent
    public static void registerMenuScreen(final RegisterMenuScreensEvent event){
        event.register(Clockware_Screens.CLOCKWARE.get(), ClockwareScreen::new);
    }
}
