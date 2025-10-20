package mors.clockware.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.mixin.accessor.EntityRenderDispatcherAccessor;
import mors.clockware.Clockware_Client;
import mors.clockware.client.model.ClockwareBodyModel;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClockwareBodyLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public final ClockwareBodyModel<T> clockwareBodyModel;

    public ClockwareBodyLayer(final RenderLayerParent<T, M> featureContext, final EntityRendererProvider.Context rendererContext) {
        super(featureContext);
        this.clockwareBodyModel = new ClockwareBodyModel<>(rendererContext.bakeLayer(Clockware_Client.CLOCKWARE_BODY_LAYER));
    }

    @Override
    public void render(@NotNull final PoseStack matrices, @NotNull final MultiBufferSource vertexConsumers, final int light,
                       @NotNull T humanoid,
                       final float limbAngle, final float limbDistance, final float tickDelta, final float animationProgress, final float headYaw, final float headPitch) {
        ClockwareItem clockwareItem= Clockware_Util.getClockwareItem(humanoid.clockware$getClockwareBySlot(ClockwareSlotType.BODY));

        if(humanoid.isInvisible() || clockwareItem==null){
            return;
        }
        
        ModelPart clockwareBody = clockwareBodyModel.body;

        int level = clockwareItem.getLevel();

        final VertexConsumer buffer =
                vertexConsumers.getBuffer(
                        RenderType.entityTranslucentCull(
                                Clockware_Util.getDefaultClockwareTexture(level, "body", clockwareItem.getClockwareName(), humanoid)));

        float time = (animationProgress + tickDelta) * (0.1f + (level*0.1f));
        float amplitude = 0.8f;


        clockwareBody.getChild("piston1").z = 2+ Mth.sin(time) * amplitude;
        clockwareBody.getChild("piston3").z = 2+ Mth.sin(time) * amplitude;

        clockwareBody.getChild("piston2").z = 2+ Mth.sin(time) * -amplitude;
        clockwareBody.getChild("piston4").z = 2+ Mth.sin(time) * -amplitude;

        clockwareBody.copyFrom(this.getParentModel().body);


        clockwareBody.render(matrices, buffer, light, OverlayTexture.NO_OVERLAY, -1);

        if(clockwareItem.hasGlowingMask()){

            VertexConsumer glowBuffer = vertexConsumers.getBuffer(RenderType.eyes(
                    Clockware_Util.getDefaultGlowClockwareTexture(level, "body", clockwareItem.getClockwareName(), humanoid)
            ));

            // Render the glowing part
            clockwareBody.render(matrices, glowBuffer, 15728640, OverlayTexture.NO_OVERLAY);
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
        ClockwareBodyLayer<?, ?> layer = new ClockwareBodyLayer<>(playerRenderer, rendererContext);
        playerRenderer.addLayer((ClockwareBodyLayer) layer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOnMob(HumanoidMobRenderer<?, ?> entityRenderer, EntityRendererProvider.Context rendererContext) {
        ClockwareBodyLayer<?, ?> layer = new ClockwareBodyLayer<>(entityRenderer, rendererContext);
        entityRenderer.addLayer((ClockwareBodyLayer) layer);
    }
}
