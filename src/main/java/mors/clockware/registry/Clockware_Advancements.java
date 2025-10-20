package mors.clockware.registry;

import mors.clockware.Clockware_Main;
import mors.clockware.Clockware_Registries;
import mors.clockware.advancement.criterion.Clockware_CustomCriterion;
import net.minecraft.advancements.CriterionTrigger;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Clockware_Advancements {

    public static final DeferredHolder<CriterionTrigger<?>, Clockware_CustomCriterion> GOLEM_HAND_CRANK =
            Clockware_Registries.CRITERIA.register(Clockware_Main.MOD_ID+"/golem_hand_crank", Clockware_CustomCriterion::new);

    public static void init() {}

}
