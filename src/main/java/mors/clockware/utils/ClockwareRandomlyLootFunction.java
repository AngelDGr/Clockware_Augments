package mors.clockware.utils;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mors.clockware.item.ClockwareItem;
import mors.clockware.registry.Clockware_Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ClockwareRandomlyLootFunction extends LootItemConditionalFunction {

    public final int level;
    public final boolean allLevels;

    public static final MapCodec<ClockwareRandomlyLootFunction> CODEC = RecordCodecBuilder
            .mapCodec(instance ->
                    ClockwareRandomlyLootFunction.commonFields(instance)
                            .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("decoctions_only").orElse(0)
                                    .forGetter(function -> function.level))
                            .and(Codec.BOOL.fieldOf("decoctions_only").orElse(false)
                                    .forGetter(function -> function.allLevels))
                            .apply(instance, ClockwareRandomlyLootFunction::new));


    protected ClockwareRandomlyLootFunction(final List<LootItemCondition> conditions, final int level, boolean allLevels) {
        super(conditions);
        this.level =level;
        this.allLevels=allLevels;
    }

    @Override
    protected @NotNull ItemStack run(@NotNull final ItemStack stack, final LootContext context) {
        final RandomSource random = context.getRandom();

        return getRandomClockware(random, this.allLevels, level);
    }
    public static ItemStack getRandomClockware(final RandomSource random, int level){
     return getRandomClockware(random, false, level);
    }

    public static ItemStack getRandomClockware(final RandomSource random, boolean allLevels, int level){
        final List<Item> listClockware = new ArrayList<>();

        BuiltInRegistries.ITEM.forEach(item -> {
                    if (item instanceof ClockwareItem clockwareItem)
                        if(!allLevels && level==clockwareItem.getLevel())
                            listClockware.add(item);
                        else if (allLevels)
                            listClockware.add(item);
                }
        );

        return new ItemStack(listClockware.get(random.nextIntBetweenInclusive(0, listClockware.size() - 1)));
    }

    @Override
    public @NotNull LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return Clockware_Items.RANDOMIZE_CLOCKWARE.get();
    }

    public static ClockwareRandomlyLootFunction.Builder create() {
        return new ClockwareRandomlyLootFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<ClockwareRandomlyLootFunction.Builder> {

        private int level;
        private boolean allLevels;

        @Override
        protected ClockwareRandomlyLootFunction.@NotNull Builder getThis() {
            return this;
        }

        public ClockwareRandomlyLootFunction.Builder add(final int decoction, final boolean allLevels) {
            this.level = decoction;
            this.allLevels = allLevels;
            return this;
        }

        @Override
        public @NotNull LootItemFunction build() {
            return new ClockwareRandomlyLootFunction(this.getConditions(), this.level, this.allLevels);
        }
    }
}
