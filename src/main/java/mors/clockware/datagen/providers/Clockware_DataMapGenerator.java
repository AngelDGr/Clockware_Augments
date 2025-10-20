package mors.clockware.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class Clockware_DataMapGenerator extends DataMapProvider {
    public Clockware_DataMapGenerator(final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
//        builder(NeoForgeDataMaps.COMPOSTABLES)
//                .add(Primal_Items.SEASHELLS, new Compostable(0.3f), false)
//                .add(Primal_Items.SHORT_RIVER_REEDS, new Compostable(0.3f), false)
//                .add(Primal_Items.RIVER_REEDS, new Compostable(0.5f), false);

    }
}
