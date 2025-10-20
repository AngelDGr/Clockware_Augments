package mors.clockware;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Clockware_Registries {

    public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Clockware_Main.MOD_ID);

    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, Clockware_Main.MOD_ID);

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Clockware_Main.MOD_ID);

    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Clockware_Main.MOD_ID);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Clockware_Main.MOD_ID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Clockware_Main.MOD_ID);

    public static DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Clockware_Main.MOD_ID);

    public static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATE_TYPES = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE, Clockware_Main.MOD_ID);

    public static DeferredRegister<CriterionTrigger<?>> CRITERIA = DeferredRegister.create(Registries.TRIGGER_TYPE, Clockware_Main.MOD_ID);

    public static DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Clockware_Main.MOD_ID);

    public static DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Clockware_Main.MOD_ID);

    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Clockware_Main.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, Clockware_Main.MOD_ID);

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, Clockware_Main.MOD_ID);

}
