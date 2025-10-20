package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import mors.clockware.item.ClockwareItem;
import mors.clockware.registry.Clockware_Tags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Clockware_ItemTagsGenerator extends ItemTagsProvider {

    public Clockware_ItemTagsGenerator(final PackOutput arg, final CompletableFuture<HolderLookup.Provider> completableFuture, final CompletableFuture<TagLookup<Block>> completableFuture2, @Nullable final ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, completableFuture2, Clockware_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {
        generateTagsForClockwareLevel(0, Clockware_Tags.PROTOTYPE_CLOCKWARE);

        generateTagsForClockwareLevel(1, Clockware_Tags.REFINED_CLOCKWARE);

        generateTagsForClockwareLevel(2, Clockware_Tags.MASTERWORK_CLOCKWARE);


        this.tag(Clockware_Tags.CLOCKWARE_ITEMS)
                .addTag(Clockware_Tags.PROTOTYPE_CLOCKWARE)
                .addTag(Clockware_Tags.REFINED_CLOCKWARE)
                .addTag(Clockware_Tags.MASTERWORK_CLOCKWARE);
    }

    private void generateTagsForClockwareLevel(int level, TagKey<Item> tagKey){
        final List<Item> clockwareList = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(item -> {
                    if (item instanceof ClockwareItem clockwareItem)
                        if(level==clockwareItem.getLevel())
                            clockwareList.add(item);
                }
        );

        var tag= this.tag(tagKey);

        for (Item item: clockwareList){
            tag.add(item);
        }
    }
}