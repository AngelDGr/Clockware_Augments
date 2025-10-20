package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class Clockware_POITypeTagGenerator extends TagsProvider<PoiType> {

    public Clockware_POITypeTagGenerator(final PackOutput arg, final CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable final ExistingFileHelper existingFileHelper) {
        super(arg, Registries.POINT_OF_INTEREST_TYPE, completableFuture, Clockware_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
                .addOptional(ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "ripperdoc_poi"));
    }
}