package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import mors.clockware.advancement.criterion.Clockware_CustomCriterion;
import mors.clockware.registry.Clockware_Items;
import mors.clockware.registry.Clockware_Tags;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Clockware_AdvancementsGenerator extends AdvancementProvider {

    public Clockware_AdvancementsGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new Generator()));
    }

    @SuppressWarnings("all")
    static final String SECRET_SUFFIX = "\n\u00A77(Hidden Advancement)"; // Replaced by "advancements.clockware.secret_suffix"

    private static final class Generator implements AdvancementGenerator {

        @SuppressWarnings("removal")
        @Override
        public void generate(final HolderLookup.@NotNull Provider registries, final @NotNull Consumer<AdvancementHolder> consumer, final @NotNull ExistingFileHelper existingFileHelper) {

            var holderAny = Advancement.Builder.advancement()
                    .parent(ResourceLocation.fromNamespaceAndPath("create", "andesite_alloy"))
                    .display(
                            Clockware_Items.OBSIDIAN_CORE_PROTOTYPE.get(), // The display icon
                            Component.translatable("advancements.clockware.get_any_clockware.title"), // The title
                            Component.translatable("advancements.clockware.get_any_clockware.description").withStyle(s -> s.withColor(0xDBA213)), // The description
                            null,
                            AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                            true, // Show toast top right
                            true, // Announce to chat
                            false // Hidden in the advancement tab
                    )
                    .addCriterion("get_any_clockware", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(Clockware_Tags.CLOCKWARE_ITEMS)))
                    .save(consumer, Clockware_Main.MOD_ID + "/get_any_clockware");


            var holderMasterwork = Advancement.Builder.advancement()
                    .parent(holderAny)
                    .display(
                            Clockware_Items.OBSIDIAN_CORE_MASTERWORK.get(), // The display icon
                            Component.translatable("advancements.clockware.get_masterwork_clockware.title"), // The title
                            Component.translatable("advancements.clockware.get_masterwork_clockware.description").withStyle(s -> s.withColor(0xDBA213)), // The description
                            null,
                            AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                            true, // Show toast top right
                            true, // Announce to chat
                            false // Hidden in the advancement tab
                    )
                    .addCriterion("get_masterwork_clockware", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(Clockware_Tags.MASTERWORK_CLOCKWARE)))
                    .save(consumer, Clockware_Main.MOD_ID + "/get_masterwork_clockware");

            Advancement.Builder.advancement()
                    .parent(holderMasterwork)
                    .display(
                            Clockware_Items.GOLEM_ARM_MASTERWORK.get(), // The display icon
                            Component.translatable("advancements.clockware.golem_hand_crank.title"), // The title
                            Component.translatable("advancements.clockware.golem_hand_crank.description").withStyle(s -> s.withColor(0xDBA213))
                                    .append(Component.translatable("advancements.clockware.secret_suffix").withStyle(ChatFormatting.GRAY)), // The description
                            null,
                            AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                            true, // Show toast top right
                            true, // Announce to chat
                            true // Hidden in the advancement tab
                    )
                    .addCriterion("golem_hand_crank", Clockware_CustomCriterion.Conditions.createGolemHandCrank())
                    .save(consumer, Clockware_Main.MOD_ID + "/golem_hand_crank");
        }
    }
}
