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
public class ClockwareRightArmModel<T extends LivingEntity> extends HumanoidModel<T> {
	public final ModelPart right_arm_wide;

	public final ModelPart right_arm_slim;

	public ClockwareRightArmModel(ModelPart root) {
        super(root);
		this.right_arm_wide = root.getChild(PartNames.RIGHT_ARM).getChild("right_arm_wide");

		this.right_arm_slim = root.getChild(PartNames.RIGHT_ARM).getChild("right_arm_slim");

	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition root = meshdefinition.getRoot();

		//Wide Arm
		{
			PartDefinition right_arm_wide = root.getChild(PartNames.RIGHT_ARM)
					.addOrReplaceChild("right_arm_wide", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.05F))
									.texOffs(16, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F))
									.texOffs(16, 16).addBox(-4.0F, 7.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
									.texOffs(23, 17).addBox(-2.0F, 7.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
							PartPose.offset(0.0F, 0.0F, 0.0F));

			//Gears Wide
			{
				PartDefinition gear1_wide = right_arm_wide.addOrReplaceChild("gear1_wide", CubeListBuilder.create().texOffs(37, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F))
						.texOffs(36, 7).addBox(-1.5F, -0.3625F, -1.5F, 3.0F, 0.725F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(38, 3).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(32, 3).addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -0.125F, 1.0F, 0.0F, 0.0F, -1.5708F));

                gear1_wide.addOrReplaceChild("Gear3_r1", CubeListBuilder.create().texOffs(37, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

                gear1_wide.addOrReplaceChild("Gear2_r1", CubeListBuilder.create().texOffs(37, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

                gear1_wide.addOrReplaceChild("Gear1_r1", CubeListBuilder.create().texOffs(37, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

                PartDefinition gear2_wide = right_arm_wide.addOrReplaceChild("gear2_wide", CubeListBuilder.create().texOffs(53, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F))
						.texOffs(52, 7).addBox(-1.5F, -0.3625F, -1.5F, 3.0F, 0.725F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(54, 3).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(48, 4).addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 3.625F, 0.25F, 0.0F, 0.0F, -1.5708F));

                gear2_wide.addOrReplaceChild("Gear3_r2", CubeListBuilder.create().texOffs(53, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

                gear2_wide.addOrReplaceChild("Gear2_r2", CubeListBuilder.create().texOffs(53, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

                gear2_wide.addOrReplaceChild("Gear1_r2", CubeListBuilder.create().texOffs(53, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
            }

			//Blade Wide
			{
				PartDefinition blade_wide = right_arm_wide.addOrReplaceChild("blade_wide", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 8.25F, 0.0F, 0.0F, 1.5708F, 3.1416F));

				PartDefinition angle2 = blade_wide.addOrReplaceChild("angle2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.5F, 1.25F, 0.3927F, 0.0F, 0.0F));

                angle2.addOrReplaceChild("half2", CubeListBuilder.create().texOffs(15, 47).mirror().addBox(-3.25F, -3.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(10, 57).addBox(-0.25F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(5, 58).addBox(0.75F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 58).addBox(1.75F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(15, 57).addBox(-2.25F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(5, 54).addBox(-1.25F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.275F, -3.875F, 0.5F, 0.0F, 0.0F, 0.7854F));

                PartDefinition angle5 = blade_wide.addOrReplaceChild("angle5", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.225F, 0.7F, 0.5F, -0.3927F, 0.0F, 0.0F));

                angle5.addOrReplaceChild("half5", CubeListBuilder.create().texOffs(10, 48).addBox(-0.5F, -2.7F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(5, 48).addBox(0.5F, -1.7F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 52).addBox(-1.5F, -1.7F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 48).addBox(1.5F, -0.7F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(5, 54).addBox(-2.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
            }

			//Claws && Launcher Wide
			{
                right_arm_wide.addOrReplaceChild("claws_wide", CubeListBuilder.create().texOffs(16, 21).addBox(-3.0F, 10.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 23).addBox(-3.0F, 11.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 21).addBox(-3.0F, 10.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 23).addBox(-3.0F, 11.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

                right_arm_wide.addOrReplaceChild("launcher_wide", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 4.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(7, 32).addBox(-5.0F, 4.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
            }

			//Fluid Tanks Wide
			{
				PartDefinition tanks_wide = right_arm_wide.addOrReplaceChild("tanks_wide", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

                tanks_wide.addOrReplaceChild("tank3", CubeListBuilder.create().texOffs(48, 19).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.025F))
                        .texOffs(48, 16).addBox(0.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 16).addBox(0.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 16).addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 16).addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 3.0F, 3.0F));

                tanks_wide.addOrReplaceChild("tank4", CubeListBuilder.create().texOffs(53, 19).addBox(-1.0F, 0.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.025F))
                        .texOffs(53, 16).addBox(-1.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(53, 16).addBox(-1.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(53, 16).addBox(-1.0F, 2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(53, 16).addBox(-1.0F, 2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));
            }
		}

		//Slim Arm
		{
			PartDefinition right_arm_slim = root.getChild(PartNames.RIGHT_ARM)
					.addOrReplaceChild("right_arm_slim", CubeListBuilder.create().texOffs(1, 0).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.05F))
									.texOffs(16, 0).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.1F))
									.texOffs(23, 17).addBox(-1.5F, 7.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
									.texOffs(16, 16).addBox(-3.0F, 7.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
							PartPose.offset(0.0F, 0.0F, 0.0F));

			//Gears Slim
			{
				PartDefinition gear1_slim = right_arm_slim.addOrReplaceChild("gear1_slim", CubeListBuilder.create().texOffs(37, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F))
						.texOffs(36, 7).addBox(-1.5F, -0.3625F, -1.5F, 3.0F, 0.725F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(38, 3).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(32, 3).addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -0.125F, 1.0F, 0.0F, 0.0F, -1.5708F));

                gear1_slim.addOrReplaceChild("Gear3_r3", CubeListBuilder.create().texOffs(37, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

                gear1_slim.addOrReplaceChild("Gear2_r3", CubeListBuilder.create().texOffs(37, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

                gear1_slim.addOrReplaceChild("Gear1_r3", CubeListBuilder.create().texOffs(37, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

                PartDefinition gear2_slim = right_arm_slim.addOrReplaceChild("gear2_slim", CubeListBuilder.create().texOffs(53, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F))
						.texOffs(52, 7).addBox(-1.5F, -0.3625F, -1.5F, 3.0F, 0.725F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(54, 3).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(48, 4).addBox(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 3.625F, 0.25F, 0.0F, 0.0F, -1.5708F));

                gear2_slim.addOrReplaceChild("Gear3_r4", CubeListBuilder.create().texOffs(53, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

                gear2_slim.addOrReplaceChild("Gear2_r4", CubeListBuilder.create().texOffs(53, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

                gear2_slim.addOrReplaceChild("Gear1_r4", CubeListBuilder.create().texOffs(53, 0).addBox(-2.25F, -0.375F, -0.375F, 4.5F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
            }

			//Blade Slim
			{
				PartDefinition blade_slim = right_arm_slim.addOrReplaceChild("blade_slim", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 8.25F, 0.0F, 0.0F, 1.5708F, 3.1416F));

				PartDefinition angle3 = blade_slim.addOrReplaceChild("angle3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.5F, 1.25F, 0.3927F, 0.0F, 0.0F));

                angle3.addOrReplaceChild("half3", CubeListBuilder.create().texOffs(15, 47).mirror().addBox(-3.25F, -3.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(10, 57).addBox(-0.25F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(5, 58).addBox(0.75F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 58).addBox(1.75F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(15, 57).addBox(-2.25F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(5, 54).addBox(-1.25F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.275F, -3.875F, 0.5F, 0.0F, 0.0F, 0.7854F));

                PartDefinition angle4 = blade_slim.addOrReplaceChild("angle4", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.225F, 0.7F, 0.5F, -0.3927F, 0.0F, 0.0F));

                angle4.addOrReplaceChild("half4", CubeListBuilder.create().texOffs(10, 48).addBox(-0.5F, -2.7F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(5, 48).addBox(0.5F, -1.7F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 52).addBox(-1.5F, -1.7F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 48).addBox(1.5F, -0.7F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(5, 54).addBox(-2.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
            }

			//Claws && Launcher Slim
			{
                right_arm_slim.addOrReplaceChild("claws_slim", CubeListBuilder.create().texOffs(16, 21).addBox(-3.0F, 10.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 23).addBox(-3.0F, 11.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 21).addBox(-3.0F, 10.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 23).addBox(-3.0F, 11.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

                right_arm_slim.addOrReplaceChild("launcher_slim", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 4.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(7, 32).addBox(-5.0F, 4.0F, -1.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));
            }

			//Fluid Tanks
			{
				PartDefinition tanks_slim = right_arm_slim.addOrReplaceChild("tanks_slim", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

                tanks_slim.addOrReplaceChild("tank1", CubeListBuilder.create().texOffs(48, 19).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.025F))
                        .texOffs(48, 16).addBox(0.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 16).addBox(0.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 16).addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 16).addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 3.0F, 3.0F));

                tanks_slim.addOrReplaceChild("tank2", CubeListBuilder.create().texOffs(53, 19).addBox(-1.0F, 0.0F, 2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.025F))
                        .texOffs(53, 16).addBox(-1.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(53, 16).addBox(-1.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(53, 16).addBox(-1.0F, 2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(53, 16).addBox(-1.0F, 2.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));
            }
		}

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
}