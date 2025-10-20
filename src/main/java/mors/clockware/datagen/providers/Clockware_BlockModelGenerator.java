package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import mors.clockware.registry.Clockware_Blocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class Clockware_BlockModelGenerator extends BlockModelProvider {

    public Clockware_BlockModelGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, Clockware_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        withExistingParent(Clockware_Blocks.RIPPERDOC_TABLE.getRegisteredName(), mcLoc("block/cube"))
                .texture("down", "clockware:block/ripperdoc_table_down")
                .texture("up", "clockware:block/ripperdoc_table_top")
                .texture("north", "clockware:block/ripperdoc_table_side1")
                .texture("south", "clockware:block/ripperdoc_table_side2")
                .texture("east", "clockware:block/ripperdoc_table_side3")
                .texture("west", "clockware:block/ripperdoc_table_side3")
                .texture("particle", "clockware:block/ripperdoc_table_side3");

    }

}