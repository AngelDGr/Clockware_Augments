package mors.clockware.datagen.providers;

import mors.clockware.Clockware_Main;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Clockware_SoundsJsonGenerator extends SoundDefinitionsProvider {

    public Clockware_SoundsJsonGenerator(final PackOutput output, final ExistingFileHelper helper) {
        super(output, Clockware_Main.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {

    }

    private void addSpecificSound(final DeferredHolder<SoundEvent, SoundEvent> sound, final String... sounds) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Clockware_Main.MOD_ID + "." + sound.getId().getPath());

        for(final String name: sounds){
            definition.with(sound(name));
        }

        this.add(sound.get(), definition);
    }

    private void addSoundEntity(final DeferredHolder<SoundEvent, SoundEvent> sound, final int soundVariationAmount) {
        final SoundDefinition definition = definition().subtitle("subtitles." + Clockware_Main.MOD_ID + "." + sound.getId().getPath());

        String[] soundPathSplitted= sound.getId().getPath().split("\\.");

        String mobType  = soundPathSplitted[1];
        String soundType= soundPathSplitted[2];

        String finalPath=mobType+"_"+soundType;

        for (int i = 1; i <= soundVariationAmount; i++)
            definition.with(sound(Clockware_Main.MOD_ID + ":entity/" + mobType + "/" + finalPath + i));

        this.add(sound.get(), definition);
    }
}
