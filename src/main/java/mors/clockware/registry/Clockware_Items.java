package mors.clockware.registry;

import com.mojang.serialization.MapCodec;
import mors.clockware.Clockware_Main;
import mors.clockware.Clockware_Registries;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareType;
import mors.clockware.item.ProjectileLauncherArm;
import mors.clockware.item.ReaperBladeArm;
import mors.clockware.utils.ClockwareRandomlyLootFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class Clockware_Items {
    //xTODO:
    //- Head -
    //Optical Calibrator: Prevents visual-distorting effects like nausea, darkness or blindness, let you see invisible players, reduces the inaccuracy of ranged weapons
    //Bio-Purifier: A mouth filter that prevents poison and hunger effects from eating, and prevents from dragon breath and harmful effects from drinkable, splash and lingering potions
    //Arachnid Optics: Makes your eyes adapt to the night, giving you night vision, increases critical damage
    // ???
    //- Legs -
    //Shock Absorber: Reduces fall damage, with two equipped makes a shockwave on landing
    //Hydrothruster: Improves swimming speed, using two gives you a whirlwind attack on water
    //Kinetic Actuator: Improve speed, using two gives you a dash
    //Jet Ankle: Improve jumping height and step-height, using two gives you double jump
    //- Chest -
    //Buoyancy Chamber: Makes it fall slower, improve elytra speed, and makes you float on water
    //Clockwork Heart: Improve natural regeneration and food eaten
    //Thermal Regulator: Reduces lava damage, negates fire damage and prevents freezing
    //Obsidian Reinforcement Core: Extra defense, reduces knockback but reduces speed
    //- Arms -
    //Reaper Blade: Lethal blade + little speed
    //Projectile Launcher: Launch system for projectiles, like arrows, and rockets!
    //Golem Arm: Extra strength + mining speed
    //Feline Hand: Extra attack speed + reach

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<ClockwareRandomlyLootFunction>> RANDOMIZE_CLOCKWARE = registerLootFunction("randomize_clockware", ClockwareRandomlyLootFunction.CODEC);


    //Head
    public static DeferredHolder<Item, Item> ARACHNID_OPTICS_PROTOTYPE;
    public static DeferredHolder<Item, Item> ARACHNID_OPTICS_REFINED;
    public static DeferredHolder<Item, Item> ARACHNID_OPTICS_MASTERWORK;

    public static DeferredHolder<Item, Item> BIOPURIFIER_PROTOTYPE;
    public static DeferredHolder<Item, Item> BIOPURIFIER_REFINED;
    public static DeferredHolder<Item, Item> BIOPURIFIER_MASTERWORK;

    public static DeferredHolder<Item, Item> OPTICAL_CALIBRATOR_PROTOTYPE;
    public static DeferredHolder<Item, Item> OPTICAL_CALIBRATOR_REFINED;
    public static DeferredHolder<Item, Item> OPTICAL_CALIBRATOR_MASTERWORK;


    //Arms
    public static DeferredHolder<Item, Item> GOLEM_ARM_PROTOTYPE;
    public static DeferredHolder<Item, Item> GOLEM_ARM_REFINED;
    public static DeferredHolder<Item, Item> GOLEM_ARM_MASTERWORK;

    public static DeferredHolder<Item, Item> REAPER_BLADE_PROTOTYPE;
    public static DeferredHolder<Item, Item> REAPER_BLADE_REFINED;
    public static DeferredHolder<Item, Item> REAPER_BLADE_MASTERWORK;

    public static DeferredHolder<Item, Item> FELINE_HAND_PROTOTYPE;
    public static DeferredHolder<Item, Item> FELINE_HAND_REFINED;
    public static DeferredHolder<Item, Item> FELINE_HAND_MASTERWORK;

    public static DeferredHolder<Item, Item> PROJECTILE_LAUNCHER_PROTOTYPE;
    public static DeferredHolder<Item, Item> PROJECTILE_LAUNCHER_REFINED;
    public static DeferredHolder<Item, Item> PROJECTILE_LAUNCHER_MASTERWORK;

    //Body
    public static DeferredHolder<Item, Item> OBSIDIAN_CORE_PROTOTYPE;
    public static DeferredHolder<Item, Item> OBSIDIAN_CORE_REFINED;
    public static DeferredHolder<Item, Item> OBSIDIAN_CORE_MASTERWORK;

    public static DeferredHolder<Item, Item> CLOCKWORK_HEART_PROTOTYPE;
    public static DeferredHolder<Item, Item> CLOCKWORK_HEART_REFINED;
    public static DeferredHolder<Item, Item> CLOCKWORK_HEART_MASTERWORK;

    public static DeferredHolder<Item, Item> BUOYANCY_CHAMBER_PROTOTYPE;
    public static DeferredHolder<Item, Item> BUOYANCY_CHAMBER_REFINED;
    public static DeferredHolder<Item, Item> BUOYANCY_CHAMBER_MASTERWORK;

    public static DeferredHolder<Item, Item> THERMAL_REGULATOR_PROTOTYPE;
    public static DeferredHolder<Item, Item> THERMAL_REGULATOR_REFINED;
    public static DeferredHolder<Item, Item> THERMAL_REGULATOR_MASTERWORK;

    //Legs
    public static DeferredHolder<Item, Item> KINETIC_ACTUATOR_PROTOTYPE;
    public static DeferredHolder<Item, Item> KINETIC_ACTUATOR_REFINED;
    public static DeferredHolder<Item, Item> KINETIC_ACTUATOR_MASTERWORK;

    public static DeferredHolder<Item, Item> JET_ANKLE_PROTOTYPE;
    public static DeferredHolder<Item, Item> JET_ANKLE_REFINED;
    public static DeferredHolder<Item, Item> JET_ANKLE_MASTERWORK;

    public static DeferredHolder<Item, Item> HYDROTHRUSTER_PROTOTYPE;
    public static DeferredHolder<Item, Item> HYDROTHRUSTER_REFINED;
    public static DeferredHolder<Item, Item> HYDROTHRUSTER_MASTERWORK;

    public static DeferredHolder<Item, Item> SHOCK_ABSORBER_PROTOTYPE;
    public static DeferredHolder<Item, Item> SHOCK_ABSORBER_REFINED;
    public static DeferredHolder<Item, Item> SHOCK_ABSORBER_MASTERWORK;

    public static DeferredHolder<Item, Item> RIPPERDOC_TABLE;


    public static void init() {

        RIPPERDOC_TABLE=register("ripperdoc_table",
                ()-> new BlockItem(Clockware_Blocks.RIPPERDOC_TABLE.get(), new Item.Properties()));

        //Head
        {
            ARACHNID_OPTICS_PROTOTYPE =register("arachnid_optics_prototype", ()-> new ClockwareItem(ClockwareType.HEAD, 0, "arachnid_optics",12, Boolean.TRUE));
            ARACHNID_OPTICS_REFINED   =register("arachnid_optics_refined", ()-> new ClockwareItem(ClockwareType.HEAD, 1, "arachnid_optics",24, Boolean.TRUE));
            ARACHNID_OPTICS_MASTERWORK=register("arachnid_optics_masterwork", ()-> new ClockwareItem(ClockwareType.HEAD, 2, "arachnid_optics",36, Boolean.TRUE));

            BIOPURIFIER_PROTOTYPE =register("biopurifier_prototype", ()-> new ClockwareItem(ClockwareType.HEAD, 0, "biopurifier",6, true));
            BIOPURIFIER_REFINED   =register("biopurifier_refined", ()-> new ClockwareItem(ClockwareType.HEAD, 1, "biopurifier",12, true));
            BIOPURIFIER_MASTERWORK=register("biopurifier_masterwork", ()-> new ClockwareItem(ClockwareType.HEAD, 2, "biopurifier",18, true));

            OPTICAL_CALIBRATOR_PROTOTYPE =register("optical_calibrator_prototype", ()-> new ClockwareItem(ClockwareType.HEAD, 0, "optical_calibrator",6, true, true));
            OPTICAL_CALIBRATOR_REFINED   =register("optical_calibrator_refined", ()-> new ClockwareItem(ClockwareType.HEAD, 1, "optical_calibrator",12, true, true));
            OPTICAL_CALIBRATOR_MASTERWORK=register("optical_calibrator_masterwork", ()-> new ClockwareItem(ClockwareType.HEAD, 2, "optical_calibrator",18, true, true));
        }

        //Arms
        {
            GOLEM_ARM_PROTOTYPE =register("golem_arm_prototype", ()-> new ClockwareItem(ClockwareType.ARM, 0, "golem_arm",8));
            GOLEM_ARM_REFINED   =register("golem_arm_refined", ()-> new ClockwareItem(ClockwareType.ARM, 1, "golem_arm",16));
            GOLEM_ARM_MASTERWORK=register("golem_arm_masterwork", ()-> new ClockwareItem(ClockwareType.ARM, 2, "golem_arm",32));

            REAPER_BLADE_PROTOTYPE =register("reaper_blade_prototype", ()-> new ReaperBladeArm(ClockwareType.ARM, 0, "reaper_blade",4));
            REAPER_BLADE_REFINED   =register("reaper_blade_refined", ()-> new ReaperBladeArm(ClockwareType.ARM, 1, "reaper_blade",8));
            REAPER_BLADE_MASTERWORK=register("reaper_blade_masterwork", ()-> new ReaperBladeArm(ClockwareType.ARM, 2, "reaper_blade",12));

            FELINE_HAND_PROTOTYPE =register("feline_hand_prototype", ()-> new ClockwareItem(ClockwareType.ARM, 0, "feline_hand",6));
            FELINE_HAND_REFINED   =register("feline_hand_refined", ()-> new ClockwareItem(ClockwareType.ARM, 1, "feline_hand",12));
            FELINE_HAND_MASTERWORK=register("feline_hand_masterwork", ()-> new ClockwareItem(ClockwareType.ARM, 2, "feline_hand",18));

            PROJECTILE_LAUNCHER_PROTOTYPE =register("projectile_launcher_prototype", ()-> new ProjectileLauncherArm(ClockwareType.ARM, 0, "projectile_launcher", 8));
            PROJECTILE_LAUNCHER_REFINED   =register("projectile_launcher_refined", ()-> new ProjectileLauncherArm(ClockwareType.ARM, 1, "projectile_launcher", 16));
            PROJECTILE_LAUNCHER_MASTERWORK=register("projectile_launcher_masterwork", ()-> new ProjectileLauncherArm(ClockwareType.ARM, 2, "projectile_launcher", 32));
        }

        //Body
        {

            OBSIDIAN_CORE_PROTOTYPE = register("obsidian_core_prototype", () -> new ClockwareItem(ClockwareType.BODY, 0, "obsidian_core", 28));
            OBSIDIAN_CORE_REFINED = register("obsidian_core_refined", () -> new ClockwareItem(ClockwareType.BODY, 1, "obsidian_core", 42));
            OBSIDIAN_CORE_MASTERWORK = register("obsidian_core_masterwork", () -> new ClockwareItem(ClockwareType.BODY, 2, "obsidian_core", 56));

            CLOCKWORK_HEART_PROTOTYPE = register("clockwork_heart_prototype", () -> new ClockwareItem(ClockwareType.BODY, 0, "clockwork_heart", 24));
            CLOCKWORK_HEART_REFINED = register("clockwork_heart_refined", () -> new ClockwareItem(ClockwareType.BODY, 1, "clockwork_heart", 36));
            CLOCKWORK_HEART_MASTERWORK = register("clockwork_heart_masterwork", () -> new ClockwareItem(ClockwareType.BODY, 2, "clockwork_heart", 48));

            BUOYANCY_CHAMBER_PROTOTYPE = register("buoyancy_chamber_prototype", () -> new ClockwareItem(ClockwareType.BODY, 0, "buoyancy_chamber", 8));
            BUOYANCY_CHAMBER_REFINED = register("buoyancy_chamber_refined", () -> new ClockwareItem(ClockwareType.BODY, 1, "buoyancy_chamber", 16));
            BUOYANCY_CHAMBER_MASTERWORK = register("buoyancy_chamber_masterwork", () -> new ClockwareItem(ClockwareType.BODY, 2, "buoyancy_chamber", 24));

            THERMAL_REGULATOR_PROTOTYPE = register("thermal_regulator_prototype", () -> new ClockwareItem(ClockwareType.BODY, 0, "thermal_regulator", 6, true));
            THERMAL_REGULATOR_REFINED = register("thermal_regulator_refined", () -> new ClockwareItem(ClockwareType.BODY, 1, "thermal_regulator", 12, true));
            THERMAL_REGULATOR_MASTERWORK = register("thermal_regulator_masterwork", () -> new ClockwareItem(ClockwareType.BODY, 2, "thermal_regulator", 24, true));
        }

        //Legs
        {
            KINETIC_ACTUATOR_PROTOTYPE = register("kinetic_actuator_prototype", () -> new ClockwareItem(ClockwareType.LEG, 0, "kinetic_actuator", 6));
            KINETIC_ACTUATOR_REFINED = register("kinetic_actuator_refined", () -> new ClockwareItem(ClockwareType.LEG, 1, "kinetic_actuator", 12));
            KINETIC_ACTUATOR_MASTERWORK = register("kinetic_actuator_masterwork", () -> new ClockwareItem(ClockwareType.LEG, 2, "kinetic_actuator", 18));

            JET_ANKLE_PROTOTYPE = register("jet_ankle_prototype", () -> new ClockwareItem(ClockwareType.LEG, 0, "jet_ankle", 3));
            JET_ANKLE_REFINED = register("jet_ankle_refined", () -> new ClockwareItem(ClockwareType.LEG, 1, "jet_ankle", 9));
            JET_ANKLE_MASTERWORK = register("jet_ankle_masterwork", () -> new ClockwareItem(ClockwareType.LEG, 2, "jet_ankle", 12));

            HYDROTHRUSTER_PROTOTYPE = register("hydrothruster_prototype", () -> new ClockwareItem(ClockwareType.LEG, 0, "hydrothruster", 3));
            HYDROTHRUSTER_REFINED = register("hydrothruster_refined", () -> new ClockwareItem(ClockwareType.LEG, 1, "hydrothruster", 9));
            HYDROTHRUSTER_MASTERWORK = register("hydrothruster_masterwork", () -> new ClockwareItem(ClockwareType.LEG, 2, "hydrothruster", 12));

            SHOCK_ABSORBER_PROTOTYPE = register("shock_absorber_prototype", () -> new ClockwareItem(ClockwareType.LEG, 0, "shock_absorber", 8));
            SHOCK_ABSORBER_REFINED = register("shock_absorber_refined", () -> new ClockwareItem(ClockwareType.LEG, 1, "shock_absorber", 12));
            SHOCK_ABSORBER_MASTERWORK = register("shock_absorber_masterwork", () -> new ClockwareItem(ClockwareType.LEG, 2, "shock_absorber", 24));
        }
    }

    public static void initGroups() {

        Clockware_Registries.CREATIVE_MODE_TABS.register(
                "item_group." + Clockware_Main.MOD_ID,
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + Clockware_Main.MOD_ID))
                        .icon(() -> new ItemStack(Clockware_Items.GOLEM_ARM_PROTOTYPE.get()))
                        .displayItems((itemDisplayParameters, output) ->
                                {
                                    output.accept(RIPPERDOC_TABLE.get());

                                    //Head
                                    {
                                        output.accept(ARACHNID_OPTICS_PROTOTYPE.get());
                                        output.accept(ARACHNID_OPTICS_REFINED.get());
                                        output.accept(ARACHNID_OPTICS_MASTERWORK.get());

                                        output.accept(BIOPURIFIER_PROTOTYPE.get());
                                        output.accept(BIOPURIFIER_REFINED.get());
                                        output.accept(BIOPURIFIER_MASTERWORK.get());

                                        output.accept(OPTICAL_CALIBRATOR_PROTOTYPE.get());
                                        output.accept(OPTICAL_CALIBRATOR_REFINED.get());
                                        output.accept(OPTICAL_CALIBRATOR_MASTERWORK.get());
                                    }

                                    //Arms
                                    {
                                        output.accept(GOLEM_ARM_PROTOTYPE.get());
                                        output.accept(GOLEM_ARM_REFINED.get());
                                        output.accept(GOLEM_ARM_MASTERWORK.get());

                                        output.accept(REAPER_BLADE_PROTOTYPE.get());
                                        output.accept(REAPER_BLADE_REFINED.get());
                                        output.accept(REAPER_BLADE_MASTERWORK.get());

                                        output.accept(FELINE_HAND_PROTOTYPE.get());
                                        output.accept(FELINE_HAND_REFINED.get());
                                        output.accept(FELINE_HAND_MASTERWORK.get());

                                        output.accept(PROJECTILE_LAUNCHER_PROTOTYPE.get());
                                        output.accept(PROJECTILE_LAUNCHER_REFINED.get());
                                        output.accept(PROJECTILE_LAUNCHER_MASTERWORK.get());
                                    }

                                    //Legs
                                    {
                                        output.accept(KINETIC_ACTUATOR_PROTOTYPE.get());
                                        output.accept(KINETIC_ACTUATOR_REFINED.get());
                                        output.accept(KINETIC_ACTUATOR_MASTERWORK.get());

                                        output.accept(JET_ANKLE_PROTOTYPE.get());
                                        output.accept(JET_ANKLE_REFINED.get());
                                        output.accept(JET_ANKLE_MASTERWORK.get());

                                        output.accept(HYDROTHRUSTER_PROTOTYPE.get());
                                        output.accept(HYDROTHRUSTER_REFINED.get());
                                        output.accept(HYDROTHRUSTER_MASTERWORK.get());

                                        output.accept(SHOCK_ABSORBER_PROTOTYPE.get());
                                        output.accept(SHOCK_ABSORBER_REFINED.get());
                                        output.accept(SHOCK_ABSORBER_MASTERWORK.get());
                                    }

                                    //Body
                                    {
                                        output.accept(OBSIDIAN_CORE_PROTOTYPE.get());
                                        output.accept(OBSIDIAN_CORE_REFINED.get());
                                        output.accept(OBSIDIAN_CORE_MASTERWORK.get());

                                        output.accept(CLOCKWORK_HEART_PROTOTYPE.get());
                                        output.accept(CLOCKWORK_HEART_REFINED.get());
                                        output.accept(CLOCKWORK_HEART_MASTERWORK.get());

                                        output.accept(BUOYANCY_CHAMBER_PROTOTYPE.get());
                                        output.accept(BUOYANCY_CHAMBER_REFINED.get());
                                        output.accept(BUOYANCY_CHAMBER_MASTERWORK.get());

                                        output.accept(THERMAL_REGULATOR_PROTOTYPE.get());
                                        output.accept(THERMAL_REGULATOR_REFINED.get());
                                        output.accept(THERMAL_REGULATOR_MASTERWORK.get());
                                    }

                                    //Head
                                    {

                                    }
                                }
                        )
                        .build()
        );

    }

    public static DeferredHolder<Item, Item> register(final String name, final Supplier<Item> item) {
        return Clockware_Registries.ITEMS.register(name, item);
    }

    private static <T extends LootItemConditionalFunction> DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<T>> registerLootFunction(String id, MapCodec<T> codec) {
        return Clockware_Registries.LOOT_FUNCTIONS.register(id, () -> new LootItemFunctionType<>(codec));
    }
}
