package mors.clockware.datagen.providers;

import com.mojang.datafixers.util.Pair;
import mors.clockware.Clockware_Main;
import mors.clockware.Clockware_Registries;
import mors.clockware.item.ClockwareItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Clockware_LanguageFileGenerator extends LanguageProvider {
    public Clockware_LanguageFileGenerator(PackOutput output) {
        super(output, Clockware_Main.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.clockware", "Clockware Augments");

        Clockware_Registries.ENTITIES.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Clockware_Main.MOD_ID))
                .forEach((entry)->addEntityTranslation(entry.getKey()));

        Clockware_Registries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Clockware_Main.MOD_ID) && !(entry.get() instanceof BlockItem))
                .forEach((entry)->addItemTranslation(entry.getKey()));

        Clockware_Registries.BLOCKS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Clockware_Main.MOD_ID))
                .forEach((entry)->addBlockTranslation(entry.getKey()));

        Clockware_Registries.MOB_EFFECTS.getEntries().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(Clockware_Main.MOD_ID))
                .forEach((entry)-> {
                    addEffectTranslation(entry.getKey());
                    addPotionTranslation(entry.getKey());
                });

        add("entity.minecraft.villager.clockware.clockware_ripperdoc", "Ripperdoc");

        //Tooltip
        {
            add("item.clockware.tooltip.level_0", "Prototype");
            add("item.clockware.tooltip.level_1", "Refined");
            add("item.clockware.tooltip.level_2", "Masterwork");

            add("item.clockware.tooltip.type_arm", "Clockware - [Arm]");
            add("item.clockware.tooltip.type_leg", "Clockware - [Leg]");
            add("item.clockware.tooltip.type_body", "Clockware - [Body]");
            add("item.clockware.tooltip.type_head", "Clockware - [Head]");

            add("item.clockware.tooltip.install", "Install with a Ripperdoc Villager");
            add("item.clockware.tooltip.extract", "Extract with a Ripperdoc Villager");

            //Head
            {
                add("item.clockware.head.arachnid_optics.tooltip.summary", "Enhances the user's _vision_, granting better _eyesight_ and extra _critical damage_.");

                add("item.clockware.head.biopurifier_0.tooltip.summary", "Integrates a _biochemical filter_ into the user's _jaw_, purifying ingested _food_ from _harmful_ _effects_.");
                add("item.clockware.head.biopurifier_1.tooltip.summary", "Integrates a _biochemical filter_ into the user's _jaw_, granting immunity to _effects_ from _food_, as well as from _drinkable_ and _splash_ _potions_.");
                add("item.clockware.head.biopurifier_2.tooltip.summary", "Integrates a _biochemical filter_ into the user's _jaw_, neutralizing toxins from _food_ and nearly all forms of _potions_ (except _arrows_). Neutralizes _dragon's_ _breath_.");

                add("item.clockware.head.optical_calibrator_0.tooltip.summary", "Equips the user with an _enhanced eye_, removing _projectile_ _inaccuracy_.");
                add("item.clockware.head.optical_calibrator_1.tooltip.summary", "Equips the user with an _enhanced eye_, removing _projectile_ _inaccuracy_ and granting immunity to _vision-distorting_ effects like _darkness_ and _nausea_.");
                add("item.clockware.head.optical_calibrator_2.tooltip.summary", "Equips the user with an _enhanced eye_, removing _projectile_ _inaccuracy_, granting immunity to _vision-distorting_ effects like _darkness_ and _nausea_, and allowing the user to _see invisible entities_.");
            }

            //Arms
            {
                add("item.clockware.arm.golem_arm.tooltip.summary", "Enhances the user's _strength_, increasing _attack_ and _mining_ _speed_.");
                add("item.clockware.arm.reaper_blade.tooltip.summary", "Deploys a hidden _blade_ from the wrist while _empty-handed_, the blade _ignores armor_.");
                add("item.clockware.arm.reaper_blade.tooltip.condition1", "Main Hand Arm");
                add("item.clockware.arm.reaper_blade.tooltip.behaviour1", "With an empty hand, hold _left-click_ (not at a block) for half a second to _extend_/_retract_ the blade.");
                add("item.clockware.arm.reaper_blade.tooltip.condition2", "Off Hand Arm");
                add("item.clockware.arm.reaper_blade.tooltip.behaviour2", "Grants passive _attack speed_ bonus.");
                add("item.clockware.arm.reaper_blade.tooltip.condition3", "With Both Arms");
                add("item.clockware.arm.reaper_blade.tooltip.behaviour3", "With _blades_ _out_, hold _right-click_ to _lunge_ (3s _cooldown_).");

                add("item.clockware.arm.feline_hand.tooltip.summary", "Enhances the user's _agility_, increasing _attacking_ _speed_ and _block_ _reach_.");

                add("item.clockware.arm.projectile_launcher.tooltip.summary", "Gives the user the ability to _shoot_ _projectiles_ from their wrist, including _arrows_, _eggs_, _snowballs_ and _rockets_.");
                add("item.clockware.arm.projectile_launcher.tooltip.condition1", "Reload from inventory");
                add("item.clockware.arm.projectile_launcher.tooltip.behaviour1", "Hold ammo and _right-click_ the launcher item _slot_.");
                add("item.clockware.arm.projectile_launcher.tooltip.condition2", "Reload while installed");
                add("item.clockware.arm.projectile_launcher.tooltip.behaviour2", "With the clockware _installed_, hold ammo in the _opposite_ hand, _sneak_, and _hold_ _right-click_.");
            }

            //Body
            {
                add("item.clockware.body.obsidian_core.tooltip.summary", "Reinforces the user's _bones_ with an internal _obsidian_ framework, greatly enhancing _defense_ and _knockback_ _resistance_ at the cost of _speed_.");

                add("item.clockware.body.clockwork_heart.tooltip.summary", "Accelerates the user's _metabolism_, pumping blood _faster_ to boost _natural_ _regeneration_ gained from _food_.");

                add("item.clockware.body.buoyancy_chamber.tooltip.summary", "Installs twin _air_ _sacs_ on the back, _regulating_ descent to reduce _fall_ _damage_, slow _sinking_ in _water_, and _enhance_ _elytra_ _acceleration_ and _speed_.");

                add("item.clockware.body.thermal_regulator_0.tooltip.summary", "Stabilizes the user's _temperature_ in harsh conditions, _reducing_ _slightly_ _fire_ and _freezing_ damage.");
                add("item.clockware.body.thermal_regulator_1.tooltip.summary", "Stabilizes the user's _temperature_ in harsh conditions, _reducing_ _greatly_ _fire_ and _freezing_ damage while _slightly_ _reducing_ _lava_ burn effects.");
                add("item.clockware.body.thermal_regulator_2.tooltip.summary", "Stabilizes the user's _temperature_ in harsh conditions, _negating_ _fire_ and _freezing_ damage while _greatly_ _reducing_ _lava_ burn effects.");

            }

            //Legs
            {
                add("item.clockware.leg.kinetic_actuator.tooltip.summary", "Enhances the user's thighs with mechanical equipment, increasing user's _movement_ _speed_.");
                add("item.clockware.leg.kinetic_actuator.tooltip.condition1", "With Both Legs");
                add("item.clockware.leg.kinetic_actuator.tooltip.behaviour1", "Double-tap _jump_ to perform a _fast_, _straight_ _dash_.");

                add("item.clockware.leg.jet_ankle.tooltip.summary", "Increases _jump height_ and _step elevation_ for smoother mobility.");
                add("item.clockware.leg.jet_ankle.tooltip.condition1", "With Both Legs");
                add("item.clockware.leg.jet_ankle.tooltip.behaviour1", "Double-tap _jump_ to perform a _double jump_.");

                add("item.clockware.leg.hydrothruster.tooltip.summary", "Equips the user with _bubble_ _thrusters_ that boost _swimming speed_.");
                add("item.clockware.leg.hydrothruster.tooltip.condition1", "With Both Legs");
                add("item.clockware.leg.hydrothruster.tooltip.behaviour1", "Double-tap _jump_ while underwater to unleash a _whirlwind_ _boost_.");

                add("item.clockware.leg.shock_absorber.tooltip.summary", "Protects the user from _impact_ through heavy metal reinforcements on the _feet_, _reducing_ _fall_ _damage_.");
                add("item.clockware.leg.shock_absorber.tooltip.condition1", "With Both Legs");
                add("item.clockware.leg.shock_absorber.tooltip.behaviour1", "Converts _reduced_ fall damage into a _shockwave_ upon landing.");
            }
        }

        //Gui
        {
            add("gui.clockware.ripperdoc.block", "Seeing Clockware");
            add("gui.clockware.ripperdoc.install_button", "Install Clockware");
            add("gui.clockware.ripperdoc.installation", "Installing Clockware");
            add("gui.clockware.ripperdoc.part.head", "Head");
            add("gui.clockware.ripperdoc.part.body", "Body");
            add("gui.clockware.ripperdoc.part.right_arm", "Arm-R");
            add("gui.clockware.ripperdoc.part.left_arm", "Arm-L");
            add("gui.clockware.ripperdoc.part.right_leg", "Leg-R");
            add("gui.clockware.ripperdoc.part.left_leg", "Leg-L");

            add("gui.clockware.ripperdoc.price.install",   "Insertion: %s Emeralds");
            add("gui.clockware.ripperdoc.price.uninstall", "Extraction: %s Emeralds");
        }

        //Advancements
        {
            add("advancements.clockware.golem_hand_crank.title", "Power Overkill");
            add("advancements.clockware.golem_hand_crank.description", "Use a Golem Arm to make a handcrank fly");

            add("advancements.clockware.get_any_clockware.title", "Metal Awakening");
            add("advancements.clockware.get_any_clockware.description", "Get your first clockware, now find a ripperdoc for installation!");

            add("advancements.clockware.get_masterwork_clockware.title", "Forged Beyond Flesh");
            add("advancements.clockware.get_masterwork_clockware.description", "Get a masterwork level clockware");

            add("clockware.secret_suffix", "\n\u00A77(Hidden Advancement)");

        }
    }

    private void addBannerTranslation(ResourceKey<BannerPattern> banner) {
        String path = banner.location().getPath();
        String baseBannerTranslation = translate(path);
        String baseBannerTranslationKey="block.minecraft.banner.primal."+path;
        add(baseBannerTranslationKey, baseBannerTranslation);

        for(DyeColor dyeColor: DyeColor.values()){
            String translatedDyeColor= translate(dyeColor.getName());
            add(baseBannerTranslationKey+"."+dyeColor.getName(), translatedDyeColor+" "+baseBannerTranslation);
        }
    }

    private void addPotionTranslation(ResourceKey<MobEffect> effectKey) {
        String effect=effectKey.location().getPath();

        List<Pair<String, String>> types=List.of(
                Pair.of("potion", "Potion of "),
                Pair.of( "splash_potion", "Splash Potion of "),
                Pair.of("lingering_potion", "Lingering Potion of "),
                Pair.of("tipped_arrow", "Arrow of "));


        for (Pair<String, String> type: types){
            String id = "item.minecraft." +type.getFirst()+ ".effect."+effect;

            String translation = type.getSecond()+translate(effect);

            add(id, translation);
        }
    }

    private void addEntityTranslation(ResourceKey<EntityType<?>> item) {
        String path = item.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.ENTITY_TYPE.get(item.location()), translation);
    }

    private void addItemTranslation(ResourceKey<Item> item) {
        String path = item.location().getPath();
        String translation = translate(path);

        //Handles descriptions and names for banner patterns
        if(BuiltInRegistries.ITEM.get(item.location()) instanceof BannerPatternItem bannerPatternItem){
            String resourceKeyPath= bannerPatternItem.getBannerPattern().location().getPath();
            String[] resourceKeySplitted = resourceKeyPath.split("/");
            //Name (All are named "Banner Pattern")
            add(BuiltInRegistries.ITEM.get(item.location()), "Banner Pattern");

            String translationBanner = translate(resourceKeySplitted[1]);
            //Description
            add(BuiltInRegistries.ITEM.get(item.location()).getDescriptionId()+".desc", translationBanner);

        } else if (BuiltInRegistries.ITEM.get(item.location()) instanceof ClockwareItem clockwareItem) {

            //Only adds translations to first level, to not repeat key
            if(clockwareItem.getLevel()==0){
                String translationBanner = translate(clockwareItem.getClockwareName());

                //Translation
                add("item.clockware."+clockwareItem.getClockwareName(), translationBanner);
            }

        } else {
            add(BuiltInRegistries.ITEM.get(item.location()), translation);
        }
    }

    private void addBlockTranslation(ResourceKey<Block> block) {
        String path = block.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.BLOCK.get(block.location()), translation);
    }

    @SuppressWarnings("all")
    private void addEffectTranslation(ResourceKey<MobEffect> block) {
        String path = block.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.MOB_EFFECT.get(block.location()), translation);
    }

    private String translate(String path) {
        return Arrays.stream(path.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
