package mors.clockware.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mors.clockware.registry.Clockware_Advancements;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class Clockware_CustomCriterion extends SimpleCriterionTrigger<Clockware_CustomCriterion.Conditions> {

    @Override
    public @NotNull Codec<Clockware_CustomCriterion.Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(final ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    public record Conditions(Optional<ContextAwarePredicate> player) implements SimpleInstance {

        public static final Codec<Clockware_CustomCriterion.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                                .forGetter(Clockware_CustomCriterion.Conditions::player))
                                .apply(instance, Clockware_CustomCriterion.Conditions::new));


        public static Criterion<Clockware_CustomCriterion.Conditions> createGolemHandCrank() {
            return Clockware_Advancements.GOLEM_HAND_CRANK.get().createCriterion(new Conditions(Optional.empty()));
        }
    }
}
