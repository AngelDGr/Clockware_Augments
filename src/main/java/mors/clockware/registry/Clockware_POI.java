package mors.clockware.registry;

import com.google.common.collect.ImmutableSet;
import mors.clockware.Clockware_Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class Clockware_POI {

    public static void init() {
        registerPoi("ripperdoc_poi", Clockware_Blocks.RIPPERDOC_TABLE);
    }

    @SuppressWarnings("all")
    public static DeferredHolder<PoiType, PoiType> registerPoi(String id, Supplier<Block> blockRegistrySupplier){
        return Clockware_Registries.POI_TYPES.register(id, () -> new PoiType(ImmutableSet.copyOf(blockRegistrySupplier.get().getStateDefinition().getPossibleStates()), 1, 1));

    }
}
