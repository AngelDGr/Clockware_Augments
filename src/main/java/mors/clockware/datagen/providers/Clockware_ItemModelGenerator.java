package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import mors.clockware.registry.Clockware_Blocks;
import mors.clockware.registry.Clockware_Items;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class Clockware_ItemModelGenerator extends ItemModelProvider {

    public Clockware_ItemModelGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Clockware_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        this.simpleBlockItem(Clockware_Blocks.RIPPERDOC_TABLE.get());

        //Head
        {
            this.basicItem(Clockware_Items.ARACHNID_OPTICS_PROTOTYPE.get());
            this.basicItem(Clockware_Items.ARACHNID_OPTICS_REFINED.get());
            this.basicItem(Clockware_Items.ARACHNID_OPTICS_MASTERWORK.get());

            this.basicItem(Clockware_Items.BIOPURIFIER_PROTOTYPE.get());
            this.basicItem(Clockware_Items.BIOPURIFIER_REFINED.get());
            this.basicItem(Clockware_Items.BIOPURIFIER_MASTERWORK.get());

            this.basicItem(Clockware_Items.OPTICAL_CALIBRATOR_PROTOTYPE.get());
            this.basicItem(Clockware_Items.OPTICAL_CALIBRATOR_REFINED.get());
            this.basicItem(Clockware_Items.OPTICAL_CALIBRATOR_MASTERWORK.get());
        }

        //Arms
        {
            this.basicItem(Clockware_Items.GOLEM_ARM_PROTOTYPE.get());
            this.basicItem(Clockware_Items.GOLEM_ARM_REFINED.get());
            this.basicItem(Clockware_Items.GOLEM_ARM_MASTERWORK.get());

            this.basicItem(Clockware_Items.REAPER_BLADE_PROTOTYPE.get());
            this.basicItem(Clockware_Items.REAPER_BLADE_REFINED.get());
            this.basicItem(Clockware_Items.REAPER_BLADE_MASTERWORK.get());

            this.basicItem(Clockware_Items.FELINE_HAND_PROTOTYPE.get());
            this.basicItem(Clockware_Items.FELINE_HAND_REFINED.get());
            this.basicItem(Clockware_Items.FELINE_HAND_MASTERWORK.get());

            this.basicItem(Clockware_Items.PROJECTILE_LAUNCHER_PROTOTYPE.get());
            this.basicItem(Clockware_Items.PROJECTILE_LAUNCHER_REFINED.get());
            this.basicItem(Clockware_Items.PROJECTILE_LAUNCHER_MASTERWORK.get());
        }

        //Body
        {
            this.basicItem(Clockware_Items.OBSIDIAN_CORE_PROTOTYPE.get());
            this.basicItem(Clockware_Items.OBSIDIAN_CORE_REFINED.get());
            this.basicItem(Clockware_Items.OBSIDIAN_CORE_MASTERWORK.get());

            this.basicItem(Clockware_Items.CLOCKWORK_HEART_PROTOTYPE.get());
            this.basicItem(Clockware_Items.CLOCKWORK_HEART_REFINED.get());
            this.basicItem(Clockware_Items.CLOCKWORK_HEART_MASTERWORK.get());

            this.basicItem(Clockware_Items.BUOYANCY_CHAMBER_PROTOTYPE.get());
            this.basicItem(Clockware_Items.BUOYANCY_CHAMBER_REFINED.get());
            this.basicItem(Clockware_Items.BUOYANCY_CHAMBER_MASTERWORK.get());

            this.basicItem(Clockware_Items.THERMAL_REGULATOR_PROTOTYPE.get());
            this.basicItem(Clockware_Items.THERMAL_REGULATOR_REFINED.get());
            this.basicItem(Clockware_Items.THERMAL_REGULATOR_MASTERWORK.get());
        }

        //Legs
        {
            this.basicItem(Clockware_Items.KINETIC_ACTUATOR_PROTOTYPE.get());
            this.basicItem(Clockware_Items.KINETIC_ACTUATOR_REFINED.get());
            this.basicItem(Clockware_Items.KINETIC_ACTUATOR_MASTERWORK.get());

            this.basicItem(Clockware_Items.JET_ANKLE_PROTOTYPE.get());
            this.basicItem(Clockware_Items.JET_ANKLE_REFINED.get());
            this.basicItem(Clockware_Items.JET_ANKLE_MASTERWORK.get());

            this.basicItem(Clockware_Items.HYDROTHRUSTER_PROTOTYPE.get());
            this.basicItem(Clockware_Items.HYDROTHRUSTER_REFINED.get());
            this.basicItem(Clockware_Items.HYDROTHRUSTER_MASTERWORK.get());

            this.basicItem(Clockware_Items.SHOCK_ABSORBER_PROTOTYPE.get());
            this.basicItem(Clockware_Items.SHOCK_ABSORBER_REFINED.get());
            this.basicItem(Clockware_Items.SHOCK_ABSORBER_MASTERWORK.get());
        }
    }


    public void basicLayered(Item item, ResourceLocation... textureLocations) {
        basicLayered(item.toString(), textureLocations);
    }

    public void basicLayered(String name, ResourceLocation... layers) {
        var builder= getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"));

        for(int i=0; i<layers.length; i++){
            builder.texture("layer"+i, layers[i]);
        }
    }
}
