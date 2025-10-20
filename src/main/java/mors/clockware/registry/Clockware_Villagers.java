package mors.clockware.registry;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import mors.clockware.Clockware_Main;
import mors.clockware.Clockware_Registries;
import mors.clockware.utils.ClockwareRandomlyLootFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;

public class Clockware_Villagers {
    public static final ResourceKey<PoiType> RIPPERDOC_POI_KEY = registerKey("ripperdoc_poi");
    public static DeferredHolder<VillagerProfession, VillagerProfession> RIPPERDOC = registerProfession("clockware_ripperdoc", RIPPERDOC_POI_KEY, null);

    public static void init(){}

    @SuppressWarnings("all")
    private static DeferredHolder<VillagerProfession, VillagerProfession> registerProfession(String name, ResourceKey<PoiType> type, SoundEvent sound) {
        return Clockware_Registries.VILLAGER_PROFESSIONS.register(name, ()->
                new VillagerProfession(name, entry -> entry.is(type), entry -> entry.is(type), ImmutableSet.of(), ImmutableSet.of(), AllSoundEvents.STEAM.getMainEvent())
        );
    }

    public static class Trades {

        public static void init(){
            //Uses            --> 16/12/3
            //Experience      --> 1/2/5/10/15/20/30
            //PriceMultiplier --> 0.05/0.2

            NeoForge.EVENT_BUS.addListener((final VillagerTradesEvent event) -> {

                //Herbalist
                if(event.getType().equals(RIPPERDOC.get())){
                    //Level 1
                    {
                        final int level = 1;
                        //Sell
                        {
                            event.getTrades().get(level)
                                    .add(new VillagerTrades.ItemsForEmeralds(
                                            //Gives
                                            AllBlocks.COGWHEEL.get(),
                                            6,
                                            2,
                                            12,
                                            2
                                    ));

                            event.getTrades().get(level)
                                    .add(new VillagerTrades.ItemsForEmeralds(
                                            //Gives
                                            AllBlocks.LARGE_COGWHEEL.get(),
                                            12,
                                            2,
                                            12,
                                            2
                                    ));
                        }

                        //Buys
                        {
                            event.getTrades().get(level)
                                    .add(new VillagerTrades.EmeraldForItems(
                                            Items.ANDESITE,
                                            12,
                                            16,
                                            2
                                    ));
                        }
                    }

                    //Level 2
                    {
                        final int level = 2;
                        //Sell
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 6 + random.nextIntBetweenInclusive(0, 8)),
                                            //Gives
                                            ClockwareRandomlyLootFunction.getRandomClockware(random, 0),
                                            3,
                                            35,
                                            0.2f));

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 6 + random.nextIntBetweenInclusive(0, 8)),
                                            //Gives
                                            ClockwareRandomlyLootFunction.getRandomClockware(random, 0),
                                            3,
                                            35,
                                            0.2f));
                        }

                        //Buys
                        {

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(AllItems.ANDESITE_ALLOY.get(), 4),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 16),
                                            12,
                                            10,
                                            0.05f));

                        }
                    }

                    //Level 3
                    {
                        final int level = 3;
                        //Sell
                        {

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 6 + random.nextIntBetweenInclusive(0, 8)),
                                            //Gives
                                            ClockwareRandomlyLootFunction.getRandomClockware(random, 0),
                                            3,
                                            35,
                                            0.2f));

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 12 + random.nextIntBetweenInclusive(0, 16)),
                                            //Gives
                                            ClockwareRandomlyLootFunction.getRandomClockware(random, 1),
                                            3,
                                            35,
                                            0.2f));
                        }

                        //Buys
                        {

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(AllItems.COPPER_SHEET.asItem(), 2),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 16),
                                            12,
                                            20,
                                            0.05f));
                        }
                    }


                    //Level 4
                    {
                        final int level = 4;
                        //Sells
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 12 + random.nextIntBetweenInclusive(0, 16)),
                                            //Gives
                                            ClockwareRandomlyLootFunction.getRandomClockware(random, 1),
                                            3,
                                            75,
                                            0.2f));

                        }

                        //Buys
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(AllItems.BRASS_SHEET.get()),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 16),
                                            12,
                                            20,
                                            0.05f));
                        }

                    }

                    //Level 5
                    {
                        final int level = 5;

                        //Buys
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 18 + random.nextIntBetweenInclusive(0, 32)),
                                            //Gives
                                            ClockwareRandomlyLootFunction.getRandomClockware(random, 2),
                                            3,
                                            10,
                                            0.2f));

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 18 + random.nextIntBetweenInclusive(0, 32)),
                                            //Gives
                                            ClockwareRandomlyLootFunction.getRandomClockware(random, 2),
                                            3,
                                            10,
                                            0.2f));
                        }

                        //Sell
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 32),
                                            //Gives
                                            new ItemStack(AllItems.BUILDERS_TEA.get()),
                                            3,
                                            10,
                                            0.2f));
                        }
                    }
                }
            });
        }

    }

    public static class Buildings {
        private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(
                Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath("minecraft", "empty"));

        private static final ResourceKey<StructureProcessorList> MOSSIFY_10_PERCENT = ResourceKey.create(
                Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath("minecraft", "mossify_10_percent"));


        private static void addBuildingToPool(final Registry<StructureTemplatePool> templatePoolRegistry,
                                              final Registry<StructureProcessorList> processorListRegistry,
                                              final ResourceLocation poolRL,
                                              final String nbtPieceRL,
                                              final int weight){
            addBuildingToPool(templatePoolRegistry, processorListRegistry, poolRL, nbtPieceRL, weight, EMPTY_PROCESSOR_LIST_KEY);
        }

        /**
         * Adds the building to the targeted pool.
         * We will call this in addNewVillageBuilding method further down to add to every village.
         *
         * Note: This is an additive operation which means multiple mods can do this and they stack with each other safely.
         */
        private static void addBuildingToPool(final Registry<StructureTemplatePool> templatePoolRegistry,
                                              final Registry<StructureProcessorList> processorListRegistry,
                                              final ResourceLocation poolRL,
                                              final String nbtPieceRL,
                                              final int weight,
                                              final ResourceKey<StructureProcessorList> processor) {
            // Grabs the processor list we want to use along with our piece.
            // This is a requirement as using the ProcessorLists.EMPTY field will cause the game to throw errors.
            // The reason why is the empty processor list in the world's registry is not the same instance as in that field once the world is started up.
            final Holder<StructureProcessorList> emptyProcessorList = processorListRegistry.getHolderOrThrow(processor);

            // Grab the pool we want to add to
            final StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
            if (pool == null) return;

            // Grabs the nbt piece and creates a SinglePoolElement of it that we can add to a structure's pool.
            // Use .legacy( for villages/outposts and .single( for everything else
            final SinglePoolElement piece = SinglePoolElement.legacy(nbtPieceRL,
                            emptyProcessorList)
                    .apply(StructureTemplatePool.Projection.RIGID);

            // Use AccessTransformer or Accessor Mixin to make StructureTemplatePool's templates field public for us to see.
            // Weight is handled by how many times the entry appears in this list.
            // We do not need to worry about immutability as this field is created using Lists.newArrayList(); which makes a mutable list.
            for (int i = 0; i < weight; i++) {
                pool.templates.add(piece);
            }

            // Use AccessTransformer or Accessor Mixin to make StructureTemplatePool's rawTemplates field public for us to see.
            // This list of pairs of pieces and weights is not used by vanilla by default but another mod may need it for efficiency.
            // So lets add to this list for completeness. We need to make a copy of the array as it can be an immutable list.
            final List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
            listOfPieceEntries.add(new Pair<>(piece, weight));
            pool.rawTemplates = listOfPieceEntries;
        }

        /**
         * We use FMLServerAboutToStartEvent as the dynamic registry exists now and all JSON worldgen files were parsed.
         * Mod compat is best done here.
         */
        public static void init() {
            NeoForge.EVENT_BUS.addListener((final ServerAboutToStartEvent event) -> {
                final Registry<StructureProcessorList> processorListRegistry = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();
                final Registry<StructureTemplatePool> templatePoolRegistry = event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).orElseThrow();

                // Adds our piece to all village houses pool
                // Note, the resourcelocation is getting the pool files from the data folder. Not assets folder.

                //Plains
                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/plains/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/plains/houses/plains_ripperdoc_house_1").toString(),
                        1,
                        MOSSIFY_10_PERCENT);

                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/plains/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/plains/houses/plains_ripperdoc_house_2").toString(),
                        2,
                        MOSSIFY_10_PERCENT
                );

                //Savanna
                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/savanna/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/savanna/houses/savanna_ripperdoc_house_1").toString(),
                        2
                );

                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/savanna/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/savanna/houses/savanna_ripperdoc_house_2").toString(),
                        1
                );

                //Desert
                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/desert/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/desert/houses/desert_ripperdoc_house_1").toString(),
                        2
                );

                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/desert/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/desert/houses/desert_ripperdoc_house_2").toString(),
                        1
                );

                //Snowy
                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/snowy/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/snowy/houses/snowy_ripperdoc_house_1").toString(),
                        1
                );

                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/snowy/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/snowy/houses/snowy_ripperdoc_house_2").toString(),
                        2
                );

                //Taiga
                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/taiga/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/taiga/houses/taiga_ripperdoc_house_1").toString(),
                        1,
                        MOSSIFY_10_PERCENT
                );

                addBuildingToPool(
                        templatePoolRegistry,
                        processorListRegistry,
                        ResourceLocation.parse("minecraft:village/taiga/houses"),
                        ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, "village/taiga/houses/taiga_ripperdoc_house_2").toString(),
                        2,
                        MOSSIFY_10_PERCENT
                );

            });
        }
    }

    public static ResourceKey<PoiType> registerKey(final String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(Clockware_Main.MOD_ID, name));
    }
}
