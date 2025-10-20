package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import mors.clockware.registry.Clockware_Blocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class Clockware_BlockStateGenerator extends BlockStateProvider {

    public Clockware_BlockStateGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Clockware_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(Clockware_Blocks.RIPPERDOC_TABLE.get(),
                new ConfiguredModel(models().getExistingFile(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "block/ripperdoc_table"))));
    }
}
