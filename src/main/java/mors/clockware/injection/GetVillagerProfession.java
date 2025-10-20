package mors.clockware.injection;

import net.minecraft.world.entity.npc.VillagerProfession;
import org.jetbrains.annotations.Nullable;

public interface GetVillagerProfession {

    default String clockware$getProfession(){return "";}

    default void clockware$setProfession(String profession){}

}
