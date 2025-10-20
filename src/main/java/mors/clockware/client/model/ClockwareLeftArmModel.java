package mors.clockware.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClockwareLeftArmModel<T extends LivingEntity> extends HumanoidModel<T> {
	public final ModelPart left_arm_wide;

	public final ModelPart left_arm_slim;

	public ClockwareLeftArmModel(ModelPart root) {
        super(root);
		this.left_arm_wide = root.getChild(PartNames.LEFT_ARM).getChild("left_arm_wide");

		this.left_arm_slim = root.getChild(PartNames.LEFT_ARM).getChild("left_arm_slim");

	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = meshdefinition.getRoot();
		//Wide
		{
			PartDefinition left_arm_wide = root.getChild(PartNames.LEFT_ARM)
					.addOrReplaceChild("left_arm_wide", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.05F)).mirror(false)
					.texOffs(16, 0).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
					.texOffs(16, 16).mirror().addBox(3.0F, 7.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
					.texOffs(23, 17).mirror().addBox(0.0F, 7.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

			//Gears Wide
			{
				PartDefinition gear1_wide = left_arm_wide.addOrReplaceChild("gear1_wide", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(36, 7).mirror().addBox(-1.5F, -0.3625F, -1.5F, 3.0F, 0.725F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(38, 3).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(32, 3).mirror().addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5F, -0.125F, 1.0F, 0.0F, 0.0F, 1.5708F));

                gear1_wide.addOrReplaceChild("Gear3_r1", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

                gear1_wide.addOrReplaceChild("Gear2_r1", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

                gear1_wide.addOrReplaceChild("Gear1_r1", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

                PartDefinition gear2_wide = left_arm_wide.addOrReplaceChild("gear2_wide", CubeListBuilder.create().texOffs(53, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(52, 7).mirror().addBox(-1.5F, -0.3625F, -1.5F, 3.0F, 0.725F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(54, 3).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(48, 4).mirror().addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5F, 3.625F, 0.25F, 0.0F, 0.0F, 1.5708F));

                gear2_wide.addOrReplaceChild("Gear3_r2", CubeListBuilder.create().texOffs(53, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

                gear2_wide.addOrReplaceChild("Gear2_r2", CubeListBuilder.create().texOffs(53, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

                gear2_wide.addOrReplaceChild("Gear1_r2", CubeListBuilder.create().texOffs(53, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
            }

			//Blade Wide
			{
				PartDefinition blade_wide = left_arm_wide.addOrReplaceChild("blade_wide", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 8.25F, 0.0F, 0.0F, -1.5708F, -3.1416F));

				PartDefinition angle2 = blade_wide.addOrReplaceChild("angle2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.5F, 1.25F, 0.3927F, 0.0F, 0.0F));

                angle2.addOrReplaceChild("half2", CubeListBuilder.create().texOffs(15, 47).addBox(0.25F, -3.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 57).mirror().addBox(-0.75F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(5, 58).mirror().addBox(-1.75F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 58).mirror().addBox(-2.75F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(15, 57).mirror().addBox(0.25F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(5, 54).mirror().addBox(0.25F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.275F, -3.875F, 0.5F, 0.0F, 0.0F, -0.7854F));

                PartDefinition angle5 = blade_wide.addOrReplaceChild("angle5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.225F, 0.7F, 0.5F, -0.3927F, 0.0F, 0.0F));

                angle5.addOrReplaceChild("half5", CubeListBuilder.create().texOffs(10, 48).mirror().addBox(-0.5F, -2.7F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(5, 48).mirror().addBox(-1.5F, -1.7F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 52).mirror().addBox(0.5F, -1.7F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 48).mirror().addBox(-2.5F, -0.7F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(5, 54).mirror().addBox(1.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
            }

			//Claws && Launcher Wide
			{
                left_arm_wide.addOrReplaceChild("claws_wide", CubeListBuilder.create().texOffs(16, 21).mirror().addBox(2.0F, 10.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(16, 23).mirror().addBox(2.0F, 11.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(16, 21).mirror().addBox(2.0F, 10.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(16, 23).mirror().addBox(2.0F, 11.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

                left_arm_wide.addOrReplaceChild("launcher_wide", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(3.0F, 4.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(7, 32).mirror().addBox(4.0F, 4.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
            }

			//Fluid Tanks Wide
			{
				PartDefinition tanks_wide = left_arm_wide.addOrReplaceChild("tanks_wide", CubeListBuilder.create(), PartPose.offset(1.0F, 0.0F, 0.0F));

                tanks_wide.addOrReplaceChild("tank3", CubeListBuilder.create().texOffs(48, 19).mirror().addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.025F)).mirror(false)
                        .texOffs(48, 16).mirror().addBox(-1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(48, 16).mirror().addBox(-1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(48, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(48, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 3.0F, 3.0F));

                tanks_wide.addOrReplaceChild("tank4", CubeListBuilder.create().texOffs(53, 19).mirror().addBox(0.0F, 0.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.025F)).mirror(false)
                        .texOffs(53, 16).mirror().addBox(0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(53, 16).mirror().addBox(0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(53, 16).mirror().addBox(0.0F, 2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(53, 16).mirror().addBox(0.0F, 2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 6.0F, 0.0F));
            }
		}

		//Slim
		{
			PartDefinition left_arm_slim = root.getChild(PartNames.LEFT_ARM)
					.addOrReplaceChild("left_arm_slim", CubeListBuilder.create().texOffs(1, 0).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.05F)).mirror(false)
					.texOffs(16, 0).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
					.texOffs(23, 17).mirror().addBox(-0.5F, 7.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
					.texOffs(16, 16).mirror().addBox(2.0F, 7.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

			//Gears Slim
			{
				PartDefinition gear1_slim = left_arm_slim.addOrReplaceChild("gear1_slim", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(36, 7).mirror().addBox(-1.5F, -0.3625F, -1.5F, 3.0F, 0.725F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(38, 3).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(32, 3).mirror().addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, -0.125F, 1.0F, 0.0F, 0.0F, 1.5708F));

                gear1_slim.addOrReplaceChild("Gear3_r3", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

                gear1_slim.addOrReplaceChild("Gear2_r3", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

                gear1_slim.addOrReplaceChild("Gear1_r3", CubeListBuilder.create().texOffs(37, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

                PartDefinition gear2_slim = left_arm_slim.addOrReplaceChild("gear2_slim", CubeListBuilder.create().texOffs(53, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(52, 7).mirror().addBox(-1.5F, -0.3625F, -1.5F, 3.0F, 0.725F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(54, 3).mirror().addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(48, 4).mirror().addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, 3.625F, 0.25F, 0.0F, 0.0F, 1.5708F));

                gear2_slim.addOrReplaceChild("Gear3_r4", CubeListBuilder.create().texOffs(53, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

                gear2_slim.addOrReplaceChild("Gear2_r4", CubeListBuilder.create().texOffs(53, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

                gear2_slim.addOrReplaceChild("Gear1_r4", CubeListBuilder.create().texOffs(53, 0).mirror().addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
            }

			//Blade Slim
			{
				PartDefinition blade_slim = left_arm_slim.addOrReplaceChild("blade_slim", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 8.25F, 0.0F, 0.0F, -1.5708F, -3.1416F));

				PartDefinition angle3 = blade_slim.addOrReplaceChild("angle3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.5F, 1.25F, 0.3927F, 0.0F, 0.0F));

                angle3.addOrReplaceChild("half3", CubeListBuilder.create().texOffs(15, 47).addBox(0.25F, -3.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 57).mirror().addBox(-0.75F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(5, 58).mirror().addBox(-1.75F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 58).mirror().addBox(-2.75F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(15, 57).mirror().addBox(0.25F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(5, 54).mirror().addBox(0.25F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.275F, -3.875F, 0.5F, 0.0F, 0.0F, -0.7854F));

                PartDefinition angle4 = blade_slim.addOrReplaceChild("angle4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.225F, 0.7F, 0.5F, -0.3927F, 0.0F, 0.0F));

                angle4.addOrReplaceChild("half4", CubeListBuilder.create().texOffs(10, 48).mirror().addBox(-0.5F, -2.7F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(5, 48).mirror().addBox(-1.5F, -1.7F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 52).mirror().addBox(0.5F, -1.7F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 48).mirror().addBox(-2.5F, -0.7F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(5, 54).mirror().addBox(1.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
            }


			//Claws && Launcher Slim
			{
                left_arm_slim.addOrReplaceChild("claws_slim", CubeListBuilder.create().texOffs(16, 21).mirror().addBox(2.0F, 10.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(16, 23).mirror().addBox(2.0F, 11.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(16, 21).mirror().addBox(2.0F, 10.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(16, 23).mirror().addBox(2.0F, 11.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 0.0F, 0.0F));

                left_arm_slim.addOrReplaceChild("launcher_slim", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(3.0F, 4.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(7, 32).mirror().addBox(4.0F, 4.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 0.0F, 0.0F));
            }

			//Fluid Tanks Slim
			{
				PartDefinition tanks_slim = left_arm_slim.addOrReplaceChild("tanks_slim", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

				PartDefinition tank1 = tanks_slim.addOrReplaceChild("tank1", CubeListBuilder.create().texOffs(48, 19).mirror().addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.025F)).mirror(false)
						.texOffs(48, 16).mirror().addBox(-1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(48, 16).mirror().addBox(-1.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(48, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(48, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 3.0F, 3.0F));

				PartDefinition bone = tank1.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

                bone.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

                tanks_slim.addOrReplaceChild("tank2", CubeListBuilder.create().texOffs(53, 19).mirror().addBox(0.0F, 0.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.025F)).mirror(false)
                        .texOffs(53, 16).mirror().addBox(0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(53, 16).mirror().addBox(0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(53, 16).mirror().addBox(0.0F, 2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(53, 16).mirror().addBox(0.0F, 2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 6.0F, 0.0F));
            }

		}

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
}