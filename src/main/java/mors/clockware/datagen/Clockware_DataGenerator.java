package mors.clockware.datagen;

import mors.clockware.Clockware_Main;
import mors.clockware.datagen.providers.*;
import mors.clockware.registry.Clockware_DamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Clockware_Main.MOD_ID)
public final class Clockware_DataGenerator {

    private static final RegistrySetBuilder BUILDER =
            new RegistrySetBuilder()
                    .add(
                            Registries.DAMAGE_TYPE, Clockware_DamageTypes::boostrapDamageTypes
                    );

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        //Client
        {
            //Block Model
            generator.addProvider(event.includeClient(), new Clockware_BlockModelGenerator(output, existingFileHelper));

            //Item Model
            generator.addProvider(event.includeClient(), new Clockware_ItemModelGenerator(output, existingFileHelper));

            //Sounds
            generator.addProvider(event.includeClient(), new Clockware_SoundsJsonGenerator(output, existingFileHelper));

            //Language File
            generator.addProvider(event.includeClient(), new Clockware_LanguageFileGenerator(output));

            //Block States
            generator.addProvider(event.includeClient(), new Clockware_BlockStateGenerator(output, existingFileHelper));
        }


        //Server
        {
            //Extra
//            generator.addProvider(true, new DatapackBuiltinEntriesProvider(output, lookupProvider, BUILDER, Set.of("minecraft", Clockware_Main.MOD_ID)));

            //Block Tags
            final var blockTagGenerator = generator.addProvider(event.includeServer(), new Clockware_BlockTagsGenerator(output, lookupProvider, existingFileHelper));
            //Damage Types Tags
            generator.addProvider(event.includeServer(), new Clockware_DamageTypesTagGenerator(output, lookupProvider, existingFileHelper));
            //Item Tags
            generator.addProvider(event.includeServer(), new Clockware_ItemTagsGenerator(output, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
            //Effects Tags
            generator.addProvider(event.includeServer(), new Clockware_MobEffectsTagsGenerator(output, lookupProvider, existingFileHelper));

            //POI Tags
            generator.addProvider(event.includeServer(), new Clockware_POITypeTagGenerator(output, lookupProvider, existingFileHelper));


            //Recipes
            generator.addProvider(event.includeServer(), new Clockware_RecipesGenerator(output, lookupProvider));

            //Advancements
            generator.addProvider(event.includeServer(), new Clockware_AdvancementsGenerator(output, lookupProvider, existingFileHelper));

            //Loot Tables
            generator.addProvider(event.includeServer(), createLootTableProviders(output, lookupProvider));

            //NeoForge Datamaps
            generator.addProvider(event.includeServer(), new Clockware_DataMapGenerator(output, lookupProvider));
        }
    }

    public static LootTableProvider createLootTableProviders(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProvider(output, Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(Clockware_LootTableBlocksGenerator::new, LootContextParamSets.BLOCK)),
                lookupProvider
        );
    }
}
