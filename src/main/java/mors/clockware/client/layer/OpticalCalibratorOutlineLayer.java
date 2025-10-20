package mors.clockware.client.layer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.foundation.mixin.accessor.EntityRenderDispatcherAccessor;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class OpticalCalibratorOutlineLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public OpticalCalibratorOutlineLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight,
                       @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        Player player = Minecraft.getInstance().player;
        if (player == null ) return;

        // Only apply for invisible entities
        if (!entity.isInvisible()) return;
        //Only while wearing the calibrator
        if (!Clockware_Util.isClockwareOnSlotSameAsType(ClockwareSlotType.HEAD, "optical_calibrator", player.clockware$getClockwareInventory())) return;

        ItemStack clockwareHead= player.clockware$getClockwareBySlot(ClockwareSlotType.HEAD);

        //Only at masterwork level
        if(clockwareHead.getItem() instanceof ClockwareItem clockwareItem && !(clockwareItem.getLevel()>1)) return;

        VertexConsumer vertex = bufferSource.getBuffer(specialOutline(this.getTextureLocation(entity)));

        var model = getParentModel();

        model.renderToBuffer(poseStack, vertex, packedLight,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                11141120);
    }

    public static RenderType specialOutline(ResourceLocation resourceLocation) {

        return RenderType.create(
                "outline_calibrator",
                DefaultVertexFormat.POSITION_TEX_COLOR,
                VertexFormat.Mode.QUADS,
                1536,
                RenderType.CompositeState.builder()
                        .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                        .setShaderState(RenderStateShard.RENDERTYPE_OUTLINE_SHADER)
                        .setCullState(RenderStateShard.NO_CULL)
//                                .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
//                                .setOutputState(RenderStateShard.OUTLINE_TARGET)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setOutputState(RenderStateShard.MAIN_TARGET)
                        .createCompositeState(RenderType.OutlineProperty.IS_OUTLINE)
        );
    }

    public static void registerOnAll(EntityRenderDispatcher renderManager) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values())
            registerOn(renderer);
        for (EntityRenderer<?> renderer : ((EntityRenderDispatcherAccessor) renderManager).create$getRenderers().values())
            registerOn(renderer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void registerOn(EntityRenderer<?> entityRenderer) {
        if (!(entityRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer))
            return;
        OpticalCalibratorOutlineLayer<?, ?> layer = new OpticalCalibratorOutlineLayer<>(livingRenderer);
        livingRenderer.addLayer((OpticalCalibratorOutlineLayer) layer);
    }
}
