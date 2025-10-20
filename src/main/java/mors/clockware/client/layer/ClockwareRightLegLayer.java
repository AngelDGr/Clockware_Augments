package mors.clockware.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.mixin.accessor.EntityRenderDispatcherAccessor;
import mors.clockware.Clockware_Client;
import mors.clockware.client.model.ClockwareRightLegModel;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClockwareRightLegLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public final ClockwareRightLegModel<T> clockwareRightLegModel;

    public ClockwareRightLegLayer(final RenderLayerParent<T, M> featureContext, final EntityRendererProvider.Context rendererContext) {
        super(featureContext);
        this.clockwareRightLegModel = new ClockwareRightLegModel<>(rendererContext.bakeLayer(Clockware_Client.CLOCKWARE_RIGHT_LEG_LAYER));
    }

    @Override
    public void render(@NotNull final PoseStack matrices, @NotNull final MultiBufferSource vertexConsumers, final int light,
                       @NotNull T humanoid,
                       final float limbAngle, final float limbDistance, final float tickDelta, final float animationProgress, final float headYaw, final float headPitch) {
        ClockwareItem clockwareItem= Clockware_Util.getClockwareItem(humanoid.clockware$getClockwareBySlot(ClockwareSlotType.RIGHT_LEG));

        if(humanoid.isInvisible() || clockwareItem==null){
            return;
        }
        
        ModelPart clockwareLeg = clockwareRightLegModel.rightLeg;

        int level = clockwareItem.getLevel();

        final VertexConsumer buffer =
                vertexConsumers.getBuffer(
                        RenderType.entityTranslucentCull(
                                Clockware_Util.getDefaultClockwareTexture(level, "leg", clockwareItem.getClockwareName(), humanoid)));

        clockwareLeg.copyFrom(this.getParentModel().rightLeg);

        clockwareLeg.render(matrices, buffer, light, OverlayTexture.NO_OVERLAY, -1);

        if(clockwareItem.hasGlowingMask()){

            VertexConsumer glowBuffer = vertexConsumers.getBuffer(RenderType.eyes(
                    Clockware_Util.getDefaultGlowClockwareTexture(level, "leg", clockwareItem.getClockwareName(), humanoid)
            ));

            // Render the glowing part
            clockwareLeg.render(matrices, glowBuffer, 15728640, OverlayTexture.NO_OVERLAY);
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
        ClockwareRightLegLayer<?, ?> layer = new ClockwareRightLegLayer<>(playerRenderer, rendererContext);
        playerRenderer.addLayer((ClockwareRightLegLayer) layer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnMob(HumanoidMobRenderer<?, ?> entityRenderer, EntityRendererProvider.Context rendererContext) {
        ClockwareRightLegLayer<?, ?> layer = new ClockwareRightLegLayer<>(entityRenderer, rendererContext);
        entityRenderer.addLayer((ClockwareRightLegLayer) layer);
    }
}
