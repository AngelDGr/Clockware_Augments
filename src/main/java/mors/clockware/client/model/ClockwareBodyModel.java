package mors.clockware.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClockwareBodyModel<T extends LivingEntity> extends HumanoidModel<T> {

    public ClockwareBodyModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0F, -0, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.05F)).texOffs(25, 0)
                .addBox(-4.0F, -0, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)),
                PartPose.offset(0.0F, 0, 0.0F));

        body.addOrReplaceChild("piston1", CubeListBuilder.create().texOffs(32, 16)
                .addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(1.5F, 1.5F, 2.0F));

        body.addOrReplaceChild("piston2", CubeListBuilder.create().texOffs(32, 16)
                .addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-1.5F, 1.5F, 2.0F));

        body.addOrReplaceChild("piston3", CubeListBuilder.create().texOffs(32, 21)
                .addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(2.5F, 3.5F, 2.0F));

        body.addOrReplaceChild("piston4", CubeListBuilder.create().texOffs(32, 21)
                .addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.5F, 3.5F, 2.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
}
