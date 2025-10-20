package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import mors.clockware.registry.Clockware_Blocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;


import java.util.Map;
import java.util.Set;

public class Clockware_LootTableBlocksGenerator extends BlockLootSubProvider{
    public Clockware_LootTableBlocksGenerator(final HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Clockware_Main.MOD_ID)).map(Map.Entry::getValue).toList();
    }

    @Override
    public void generate() {
        this.dropSelf(Clockware_Blocks.RIPPERDOC_TABLE.get());
    }
}
