package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import mors.clockware.registry.Clockware_Tags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class Clockware_MobEffectsTagsGenerator extends TagsProvider<MobEffect> {

    public Clockware_MobEffectsTagsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.MOB_EFFECT, lookupProvider, Clockware_Main.MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags(final HolderLookup.@NotNull Provider lookup) {

        this.tag(Clockware_Tags.OPTICAL_CALIBRATOR_EFFECT_IMMUNITY)
                .add(MobEffects.CONFUSION.getKey())
                .add(MobEffects.DARKNESS.getKey())
                .add(MobEffects.BLINDNESS.getKey());

    }
}