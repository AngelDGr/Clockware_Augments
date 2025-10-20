package mors.clockware.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.mixin.accessor.EntityRenderDispatcherAccessor;
import mors.clockware.Clockware_Client;
import mors.clockware.client.model.ClockwareLeftArmModel;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.registry.Clockware_Components;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClockwareLeftArmLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public final ClockwareLeftArmModel<T> clockwareLeftArmModel;

    public ClockwareLeftArmLayer(final RenderLayerParent<T, M> featureContext, final EntityRendererProvider.Context rendererContext) {
        super(featureContext);
        this.clockwareLeftArmModel = new ClockwareLeftArmModel<>(rendererContext.bakeLayer(Clockware_Client.CLOCKWARE_LEFT_ARM_LAYER));
    }

    @Override
    public void render(@NotNull final PoseStack matrices, @NotNull final MultiBufferSource vertexConsumers, final int light,
                       @NotNull T humanoid,
                       final float limbAngle, final float limbDistance, final float tickDelta, final float animationProgress, final float headYaw, final float headPitch) {

        ClockwareItem clockwareItem= Clockware_Util.getClockwareItem(humanoid.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_ARM));

        if(humanoid.isInvisible() || clockwareItem==null){
            return;
        }

        // Choose slim or wide arm
        ModelPart clockwareArm = Clockware_Util.isSlim(humanoid)
                ? clockwareLeftArmModel.left_arm_slim
                : clockwareLeftArmModel.left_arm_wide;

        int level = clockwareItem.getLevel();

        final VertexConsumer buffer =
                vertexConsumers.getBuffer(
                        RenderType.entityTranslucentCull(
                                Clockware_Util.getArmClockwareTexture(level, "arm", clockwareItem.getClockwareName(), humanoid)));

        clockwareArm.copyFrom(this.getParentModel().leftArm);

        // Animate gears
        float spin = (humanoid.tickCount + tickDelta) * (0.1F+(level*0.1f)); // speed factor
        clockwareArm.getChild("gear1_"+ Clockware_Util.getSlimString(humanoid)).yRot = spin;
        clockwareArm.getChild("gear2_"+ Clockware_Util.getSlimString(humanoid)).yRot = -spin;

        //Disables the blade as default
        clockwareArm.getChild("blade_"+ Clockware_Util.getSlimString(humanoid)).visible=false;

        ItemStack clockwareStack= humanoid.clockware$getClockwareBySlot(ClockwareSlotType.LEFT_ARM);
        var bladeOut = clockwareStack.get(Clockware_Components.BLADE_OUT);

        if(bladeOut!=null){
            // Activates blade
            clockwareArm.getChild("blade_"+ Clockware_Util.getSlimString(humanoid)).visible=bladeOut;
        }

        clockwareArm.render(matrices, buffer, light, OverlayTexture.NO_OVERLAY, -1);

        if(clockwareItem.hasGlowingMask()){

            VertexConsumer glowBuffer =
                    vertexConsumers.getBuffer(
                            RenderType.entityTranslucentCull(
                                    Clockware_Util.getArmGlowClockwareTexture(level, "arm", clockwareItem.getClockwareName(), humanoid)));

            // Render the glowing part
            clockwareArm.render(matrices, glowBuffer, 15728640, OverlayTexture.NO_OVERLAY);
        }
    }

    public static void registerOnAll(EntityRenderDispatcher renderManager, EntityRendererProvider.Context rendererContext) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values())
            if(renderer instanceof PlayerRenderer playerRenderer)
                registerOnPlayer(playerRenderer, rendererContext);
        for (EntityRenderer<?> renderer : ((EntityRenderDispatcherAccessor) renderManager).create$getRenderers().values())
            if(renderer instanceof HumanoidMobRenderer<?,?> humanoidMobRenderer)
                registerOnMob(humanoidMobRenderer, rendererContext);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnPlayer(PlayerRenderer playerRenderer, EntityRendererProvider.Context rendererContext) {
        ClockwareLeftArmLayer<?, ?> layer = new ClockwareLeftArmLayer<>(playerRenderer, rendererContext);
        playerRenderer.addLayer((ClockwareLeftArmLayer) layer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnMob(HumanoidMobRenderer<?, ?> entityRenderer, EntityRendererProvider.Context rendererContext) {
        ClockwareLeftArmLayer<?, ?> layer = new ClockwareLeftArmLayer<>(entityRenderer, rendererContext);
        entityRenderer.addLayer((ClockwareLeftArmLayer) layer);
    }

}
