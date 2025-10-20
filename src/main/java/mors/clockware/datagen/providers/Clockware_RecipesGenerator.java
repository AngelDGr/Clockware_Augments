package mors.clockware.datagen.providers;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import mors.clockware.registry.Clockware_Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class Clockware_RecipesGenerator extends RecipeProvider {

    public Clockware_RecipesGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public void buildRecipes(final @NotNull RecipeOutput exporter) {

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Clockware_Items.RIPPERDOC_TABLE.get())
                .define('A', AllItems.ANDESITE_ALLOY.asItem())
                .define('C', AllItems.COPPER_SHEET.asItem())

                .define('W', ItemTags.PLANKS)

                .pattern("AC")
                .pattern("WW")
                .pattern("WW")
                .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                .save(exporter);

        //Head
        {
            //Prototype
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.ARACHNID_OPTICS_PROTOTYPE.get())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('S', AllBlocks.SHAFT.asItem())

                        .define('E', Items.SPIDER_EYE)

                        .pattern("EZE")
                        .pattern("ESE")
                        .pattern("AZA")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.BIOPURIFIER_PROTOTYPE.get())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('S', AllBlocks.SHAFT.asItem())

                        .define('H', Items.HONEYCOMB)

                        .pattern("AZA")
                        .pattern("ASA")
                        .pattern("HZH")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.OPTICAL_CALIBRATOR_PROTOTYPE.get())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('S', AllBlocks.SHAFT.asItem())

                        .define('M', Items.AMETHYST_SHARD)

                        .pattern("AZA")
                        .pattern("MSM")
                        .pattern("AZA")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);
            }

            //Refined
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.ARACHNID_OPTICS_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('G', Items.GOLDEN_CARROT)
                        .define('P', Clockware_Items.ARACHNID_OPTICS_PROTOTYPE.get())

                        .pattern("GCG")
                        .pattern("CPC")
                        .pattern("GCG")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.BIOPURIFIER_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('H', Items.HONEYCOMB)
                        .define('B', Items.BLAZE_POWDER)
                        .define('P', Clockware_Items.BIOPURIFIER_PROTOTYPE.get())

                        .pattern("BCB")
                        .pattern("HPH")
                        .pattern("BCB")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.OPTICAL_CALIBRATOR_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('G', Items.TINTED_GLASS)
                        .define('P', Clockware_Items.OPTICAL_CALIBRATOR_PROTOTYPE.get())

                        .pattern("CGC")
                        .pattern("GPG")
                        .pattern("CGC")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);
            }

            //Masterwork
            {
                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.ARACHNID_OPTICS_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())

                        .key('R', Clockware_Items.ARACHNID_OPTICS_REFINED.get())
                        .key('M', AllItems.PRECISION_MECHANISM.asItem())
                        .key('E', AllItems.ELECTRON_TUBE.asItem())

                        .patternLine(" EBE ")
                        .patternLine("BMRMB")
                        .patternLine(" BBB ")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.BIOPURIFIER_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())

                        .key('R', Clockware_Items.BIOPURIFIER_REFINED.get())
                        .key('H', Items.HONEYCOMB_BLOCK)
                        .key('S', AllItems.STURDY_SHEET.asItem())

                        .patternLine(" BBB ")
                        .patternLine("BHRHB")
                        .patternLine(" SSS ")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.OPTICAL_CALIBRATOR_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())

                        .key('R', Clockware_Items.OPTICAL_CALIBRATOR_REFINED.get())
                        .key('M', AllItems.PRECISION_MECHANISM.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())
                        .key('E', AllItems.ELECTRON_TUBE.asItem())

                        .patternLine(" EBE ")
                        .patternLine("SMRMS")
                        .patternLine(" EBE ")
                        .build(exporter);
            }
        }

        //Arms
        {
            //Prototype
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.GOLEM_ARM_PROTOTYPE.get())
                        .define('W', AllBlocks.WATER_WHEEL.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('Z', AllItems.ZINC_INGOT.asItem())

                        .define('G', AllBlocks.COGWHEEL.asItem())
                        .define('P', AllBlocks.MECHANICAL_PRESS.asItem())
                        .define('B', Items.STONE_BUTTON)

                        .pattern("GWZ")
                        .pattern("GPA")
                        .pattern("ABA")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.REAPER_BLADE_PROTOTYPE.get())
                        .define('W', AllBlocks.WATER_WHEEL.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('Z', AllItems.ZINC_INGOT.asItem())

                        .define('P', Items.STICKY_PISTON)
                        .define('S', Items.IRON_SWORD)

                        .pattern("AWZ")
                        .pattern("SPA")
                        .pattern("AAA")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.FELINE_HAND_PROTOTYPE.get())
                        .define('W', AllBlocks.WATER_WHEEL.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('Z', AllItems.ZINC_INGOT.asItem())

                        .define('L', AllBlocks.LARGE_COGWHEEL.asItem())
                        .define('G', AllBlocks.COGWHEEL.asItem())
                        .define('I', AllItems.IRON_SHEET.asItem())

                        .pattern("GWZ")
                        .pattern("ALA")
                        .pattern("IAI")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.PROJECTILE_LAUNCHER_PROTOTYPE.get())
                        .define('W', AllBlocks.WATER_WHEEL.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('Z', AllItems.ZINC_INGOT.asItem())

                        .define('D', Items.DISPENSER)
                        .define('R', Items.REDSTONE_BLOCK)

                        .pattern("AWZ")
                        .pattern("DRA")
                        .pattern("DAA")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);
            }

            //Refined
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.GOLEM_ARM_REFINED.get())
                        .define('F', AllBlocks.FLUID_TANK.asItem())
                        .define('E', AllBlocks.STEAM_ENGINE.asItem())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('G', AllBlocks.COGWHEEL.asItem())
                        .define('P', Clockware_Items.GOLEM_ARM_PROTOTYPE.get())

                        .pattern("GFF")
                        .pattern("GEC")
                        .pattern("CPC")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.REAPER_BLADE_REFINED.get())
                        .define('F', AllBlocks.FLUID_TANK.asItem())
                        .define('E', AllBlocks.STEAM_ENGINE.asItem())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('S', Items.DIAMOND_SWORD)
                        .define('P', Clockware_Items.REAPER_BLADE_PROTOTYPE.get())

                        .pattern("CFC")
                        .pattern("SEC")
                        .pattern("CPC")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.FELINE_HAND_REFINED.get())
                        .define('F', AllBlocks.FLUID_TANK.asItem())
                        .define('E', AllBlocks.STEAM_ENGINE.asItem())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('G', AllBlocks.COGWHEEL.asItem())
                        .define('S', AllItems.GOLDEN_SHEET.asItem())
                        .define('P', Clockware_Items.FELINE_HAND_PROTOTYPE.get())

                        .pattern("GFC")
                        .pattern("CEC")
                        .pattern("SPS")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.PROJECTILE_LAUNCHER_REFINED.get())
                        .define('F', AllBlocks.FLUID_TANK.asItem())
                        .define('E', AllBlocks.STEAM_ENGINE.asItem())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('B', Items.COPPER_BLOCK)
                        .define('D', Items.DISPENSER)
                        .define('P', Clockware_Items.PROJECTILE_LAUNCHER_PROTOTYPE.get())

                        .pattern("CFC")
                        .pattern("DEC")
                        .pattern("BPC")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);
            }

            //Masterwork
            {
                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.GOLEM_ARM_MASTERWORK.get())
                        .key('E', AllItems.ELECTRON_TUBE.asItem())
                        .key('B', AllItems.BRASS_SHEET.asItem())
                        .key('M', AllItems.PRECISION_MECHANISM.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())

                        .key('X', AllBlocks.BRASS_BLOCK.asItem())
                        .key('R', Clockware_Items.GOLEM_ARM_REFINED.get())

                        .patternLine("XEB")
                        .patternLine("XMB")
                        .patternLine("BRB")
                        .patternLine("SMB")
                        .patternLine("SSS")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.REAPER_BLADE_MASTERWORK.get())
                        .key('E', AllItems.ELECTRON_TUBE.asItem())
                        .key('B', AllItems.BRASS_SHEET.asItem())
                        .key('M', AllItems.PRECISION_MECHANISM.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())

                        .key('X', Items.NETHERITE_SWORD)
                        .key('R', Clockware_Items.REAPER_BLADE_REFINED.get())

                        .patternLine("BEB")
                        .patternLine("BMB")
                        .patternLine("XRB")
                        .patternLine("BMB")
                        .patternLine("SSS")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.FELINE_HAND_MASTERWORK.get())
                        .key('E', AllItems.ELECTRON_TUBE.asItem())
                        .key('B', AllItems.BRASS_SHEET.asItem())
                        .key('M', AllItems.PRECISION_MECHANISM.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())

                        .key('X', Items.DIAMOND)
                        .key('R', Clockware_Items.FELINE_HAND_REFINED.get())

                        .patternLine("BEB")
                        .patternLine("BMB")
                        .patternLine("BRB")
                        .patternLine("SMS")
                        .patternLine("XSX")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.PROJECTILE_LAUNCHER_MASTERWORK.get())
                        .key('E', AllItems.ELECTRON_TUBE.asItem())
                        .key('B', AllItems.BRASS_SHEET.asItem())
                        .key('M', AllItems.PRECISION_MECHANISM.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())

                        .key('X', AllBlocks.BRASS_BLOCK.asItem())
                        .key('R', Clockware_Items.PROJECTILE_LAUNCHER_REFINED.get())

                        .patternLine("BEB")
                        .patternLine("XMB")
                        .patternLine("XRB")
                        .patternLine("BMB")
                        .patternLine("SSS")
                        .build(exporter);
            }
        }

        //Body
        {
            //Prototype
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.OBSIDIAN_CORE_PROTOTYPE.get())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('C', AllBlocks.LARGE_COGWHEEL.asItem())

                        .define('O', Items.OBSIDIAN)

                        .pattern("AZA")
                        .pattern("OCO")
                        .pattern("AOA")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.CLOCKWORK_HEART_PROTOTYPE.get())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('C', AllBlocks.LARGE_COGWHEEL.asItem())

                        .define('G', AllItems.GOLDEN_SHEET.asItem())
                        .define('S', AllBlocks.COGWHEEL.asItem())
                        .define('W', AllItems.WHISK.asItem())

                        .pattern("GZG")
                        .pattern("ACA")
                        .pattern("SWS")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.BUOYANCY_CHAMBER_PROTOTYPE.get())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('C', AllBlocks.LARGE_COGWHEEL.asItem())

                        .define('M', Items.PHANTOM_MEMBRANE)

                        .pattern("AZA")
                        .pattern("MCM")
                        .pattern("AMA")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.THERMAL_REGULATOR_PROTOTYPE.get())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('C', AllBlocks.LARGE_COGWHEEL.asItem())

                        .define('I', Items.ICE)
                        .define('L', Items.LAVA_BUCKET)

                        .pattern("AZA")
                        .pattern("ICI")
                        .pattern("ALA")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);
            }

            //Refined
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.OBSIDIAN_CORE_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('B', Items.COPPER_BLOCK)
                        .define('P', Clockware_Items.OBSIDIAN_CORE_PROTOTYPE.get())

                        .pattern("CBC")
                        .pattern("BPB")
                        .pattern("CBC")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.CLOCKWORK_HEART_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('S', AllBlocks.STEAM_ENGINE.asItem())
                        .define('P', Clockware_Items.CLOCKWORK_HEART_PROTOTYPE.get())

                        .pattern("SSS")
                        .pattern("CPC")
                        .pattern("CCC")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.BUOYANCY_CHAMBER_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('M', Items.PHANTOM_MEMBRANE)
                        .define('P', Clockware_Items.BUOYANCY_CHAMBER_PROTOTYPE.get())

                        .pattern("CMC")
                        .pattern("MPM")
                        .pattern("CMC")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.THERMAL_REGULATOR_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('I', Items.PACKED_ICE)
                        .define('E', AllBlocks.FLUID_PIPE.asItem())
                        .define('P', Clockware_Items.THERMAL_REGULATOR_PROTOTYPE.get())

                        .pattern("ECE")
                        .pattern("IPI")
                        .pattern("ECE")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);
            }

            //Masterwork
            {
                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.OBSIDIAN_CORE_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())

                        .key('P', AllItems.POWDERED_OBSIDIAN.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())
                        .key('R', Clockware_Items.OBSIDIAN_CORE_REFINED.get())

                        .patternLine("  S  ")
                        .patternLine(" SPS ")
                        .patternLine("BPRPB")
                        .patternLine(" BPB ")
                        .patternLine("  B  ")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.CLOCKWORK_HEART_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())

                        .key('P', AllBlocks.BLAZE_BURNER.asItem())
                        .key('M', AllBlocks.MECHANICAL_MIXER.asItem())
                        .key('R', Clockware_Items.CLOCKWORK_HEART_REFINED.get())

                        .patternLine("  B  ")
                        .patternLine(" BMB ")
                        .patternLine("BPRPB")
                        .patternLine(" BPB ")
                        .patternLine("  B  ")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.BUOYANCY_CHAMBER_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())

                        .key('M', Items.PHANTOM_MEMBRANE)
                        .key('Z', Items.BREEZE_ROD)
                        .key('R', Clockware_Items.BUOYANCY_CHAMBER_REFINED.get())

                        .patternLine("  B  ")
                        .patternLine(" BMB ")
                        .patternLine("BZRZB")
                        .patternLine(" BMB ")
                        .patternLine("  B  ")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.THERMAL_REGULATOR_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())

                        .key('I', Items.BLUE_ICE)
                        .key('P', AllBlocks.BLAZE_BURNER.asItem())
                        .key('R', Clockware_Items.THERMAL_REGULATOR_REFINED.get())

                        .patternLine("  B  ")
                        .patternLine(" BIB ")
                        .patternLine("BIRIB")
                        .patternLine(" BPB ")
                        .patternLine("  B  ")
                        .build(exporter);
            }
        }

        //Legs
        {
            //Prototype
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.KINETIC_ACTUATOR_PROTOTYPE.get())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('C', AllBlocks.COGWHEEL.asItem())

                        .define('I', AllItems.IRON_SHEET)
                        .define('S', Items.SUGAR)

                        .pattern("AZA")
                        .pattern("ACI")
                        .pattern("ASI")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.JET_ANKLE_PROTOTYPE.get())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('C', AllBlocks.COGWHEEL.asItem())

                        .define('P', AllItems.PROPELLER)
                        .define('R', Items.RABBIT_FOOT)

                        .pattern("AZA")
                        .pattern("ACA")
                        .pattern("PRP")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.HYDROTHRUSTER_PROTOTYPE.get())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('C', AllBlocks.COGWHEEL.asItem())

                        .define('M', Items.MAGMA_BLOCK)

                        .pattern("AZA")
                        .pattern("ACA")
                        .pattern("MAM")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.SHOCK_ABSORBER_PROTOTYPE.get())
                        .define('Z', AllItems.ZINC_INGOT.asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.asItem())
                        .define('C', AllBlocks.COGWHEEL.asItem())

                        .define('B', Items.IRON_BLOCK)
                        .define('W', ItemTags.WOOL)

                        .pattern("AZA")
                        .pattern("ACA")
                        .pattern("BWB")
                        .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY.asItem()))
                        .save(exporter);
            }

            //Refined
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.KINETIC_ACTUATOR_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('F', Tags.Items.FEATHERS)
                        .define('S', Items.SUGAR)
                        .define('P', Clockware_Items.KINETIC_ACTUATOR_PROTOTYPE.get())

                        .pattern("CSC")
                        .pattern("FPF")
                        .pattern("CSC")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.JET_ANKLE_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('B', Items.COPPER_BLOCK)
                        .define('W', Items.WIND_CHARGE)
                        .define('P', Clockware_Items.JET_ANKLE_PROTOTYPE.get())

                        .pattern("CCC")
                        .pattern("BPB")
                        .pattern("WCW")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.HYDROTHRUSTER_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('G', AllItems.GOLDEN_SHEET)
                        .define('P', Clockware_Items.HYDROTHRUSTER_PROTOTYPE.get())

                        .pattern("GCG")
                        .pattern("CPC")
                        .pattern("GCG")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Clockware_Items.SHOCK_ABSORBER_REFINED.get())
                        .define('C', AllItems.COPPER_SHEET.asItem())

                        .define('B', Items.COPPER_BLOCK)
                        .define('P', Clockware_Items.SHOCK_ABSORBER_PROTOTYPE.get())

                        .pattern("CCC")
                        .pattern("CPC")
                        .pattern("BBB")
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(exporter);

            }

            //Masterwork
            {
                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.KINETIC_ACTUATOR_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())

                        .key('F', Tags.Items.FEATHERS)
                        .key('M', AllItems.PRECISION_MECHANISM)
                        .key('R', Clockware_Items.KINETIC_ACTUATOR_REFINED.get())

                        .patternLine("BBB")
                        .patternLine("BMB")
                        .patternLine("SRS")
                        .patternLine("FMF")
                        .patternLine("BBB")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.JET_ANKLE_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())

                        .key('W', Items.WIND_CHARGE)
                        .key('M', AllItems.PRECISION_MECHANISM)
                        .key('R', Clockware_Items.JET_ANKLE_REFINED.get())

                        .patternLine("BBB")
                        .patternLine("BMB")
                        .patternLine("SRS")
                        .patternLine("SWS")
                        .patternLine("BBB")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.HYDROTHRUSTER_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())

                        .key('P', AllItems.PROPELLER)
                        .key('M', AllItems.PRECISION_MECHANISM)
                        .key('R', Clockware_Items.HYDROTHRUSTER_REFINED.get())

                        .patternLine("BBB")
                        .patternLine("SMS")
                        .patternLine("SRS")
                        .patternLine("PMP")
                        .patternLine("BBB")
                        .build(exporter);

                MechanicalCraftingRecipeBuilder.shapedRecipe(Clockware_Items.SHOCK_ABSORBER_MASTERWORK.get())
                        .key('B', AllItems.BRASS_SHEET.asItem())

                        .key('A', AllBlocks.BRASS_BLOCK.asItem())
                        .key('S', AllItems.STURDY_SHEET.asItem())
                        .key('W', ItemTags.WOOL)
                        .key('R', Clockware_Items.SHOCK_ABSORBER_REFINED.get())

                        .patternLine("BBB")
                        .patternLine("SWS")
                        .patternLine("SRS")
                        .patternLine("BWB")
                        .patternLine("AAA")
                        .build(exporter);

            }
        }
    }
}
