package mors.clockware.registry;

import mors.clockware.Clockware_Registries;
import mors.clockware.block.RipperdocTableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class Clockware_Blocks {

    public static DeferredHolder<Block, Block> RIPPERDOC_TABLE;

    public static void init(){

        RIPPERDOC_TABLE=register("ripperdoc_table",
                ()-> new RipperdocTableBlock(
                        BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava()
                ));
    }

    public static DeferredHolder<Block, Block> register(final String name, final Supplier<Block> block) {
        return Clockware_Registries.BLOCKS.register(name, block);
    }
}
