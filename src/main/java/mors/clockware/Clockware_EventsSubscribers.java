package mors.clockware;

import mors.clockware.event.ClockwareDoubleJumpEvent;
import mors.clockware.event.ClockwareLeftClickEvent;
import mors.clockware.event.ClockwareRightClickEvent;
import mors.clockware.event.ClockwareTickEvent;
import mors.clockware.item.ClockwareItem;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.item.ProjectileLauncherArm;
import mors.clockware.item.ReaperBladeArm;
import mors.clockware.item.component.ProjectileArmStorage;
import mors.clockware.registry.Clockware_Components;
import mors.clockware.registry.Clockware_Items;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

@EventBusSubscriber(modid = Clockware_Main.MOD_ID)
public class Clockware_EventsSubscribers {

    //To avoid launching projectiles while recharging an arm
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {

        for (HumanoidArm arm: HumanoidArm.values()){
            avoidRightClickIfReloading(event, arm, Clockware_Items.PROJECTILE_LAUNCHER_MASTERWORK.get());

            avoidRightClickIfReloading(event, arm, Clockware_Items.PROJECTILE_LAUNCHER_REFINED.get());

            avoidRightClickIfReloading(event, arm, Clockware_Items.PROJECTILE_LAUNCHER_PROTOTYPE.get());
        }
    }

    private static void avoidRightClickIfReloading(PlayerInteractEvent.RightClickItem event, HumanoidArm arm, Item launcherItem){
        ClockwareSlotType slot = arm.equals(HumanoidArm.RIGHT)? ClockwareSlotType.RIGHT_ARM: ClockwareSlotType.LEFT_ARM;

        Player player = event.getEntity();

        boolean hasLauncherInArm =
                ItemStack.isSameItem(event.getEntity().clockware$getClockwareBySlot(slot),
                        launcherItem.getDefaultInstance());

        boolean isReloading = player.isShiftKeyDown() && hasLauncherInArm;

        // If reloading, prevent Minecraft from using the projectile item
        if (isReloading &&
                (event.getEntity().getItemInHand(event.getEntity().getMainArm().equals(arm)? InteractionHand.OFF_HAND:  InteractionHand.MAIN_HAND).getItem() instanceof ProjectileItem)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void clockwareTick(final ClockwareTickEvent event){

        //Body Attributes
        {

            //Maps the items with the attributes
            Map<Item, Map<Holder<Attribute>, AttributeModifier>> bodyDefinitions = Map.of(
                    //Prototype
                    Clockware_Items.OBSIDIAN_CORE_PROTOTYPE.get(), Map.of(
                            Attributes.ARMOR, Clockware_Modifiers.ARMOR_OBSIDIAN_CORE_0,
                            Attributes.ARMOR_TOUGHNESS, Clockware_Modifiers.TOUGHNESS_OBSIDIAN_CORE_0,
                            Attributes.KNOCKBACK_RESISTANCE, Clockware_Modifiers.KNOCKBACK_OBSIDIAN_CORE_0,
                            Attributes.MOVEMENT_SPEED, Clockware_Modifiers.SPEED_OBSIDIAN_CORE_0
                    ),
                    //Refined
                    Clockware_Items.OBSIDIAN_CORE_REFINED.get(), Map.of(
                            Attributes.ARMOR, Clockware_Modifiers.ARMOR_OBSIDIAN_CORE_1,
                            Attributes.ARMOR_TOUGHNESS, Clockware_Modifiers.TOUGHNESS_OBSIDIAN_CORE_1,
                            Attributes.KNOCKBACK_RESISTANCE, Clockware_Modifiers.KNOCKBACK_OBSIDIAN_CORE_1,
                            Attributes.MOVEMENT_SPEED, Clockware_Modifiers.SPEED_OBSIDIAN_CORE_1
                    ),
                    //Masterwork
                    Clockware_Items.OBSIDIAN_CORE_MASTERWORK.get(), Map.of(
                            Attributes.ARMOR, Clockware_Modifiers.ARMOR_OBSIDIAN_CORE_2,
                            Attributes.ARMOR_TOUGHNESS, Clockware_Modifiers.TOUGHNESS_OBSIDIAN_CORE_2,
                            Attributes.KNOCKBACK_RESISTANCE, Clockware_Modifiers.KNOCKBACK_OBSIDIAN_CORE_2,
                            Attributes.MOVEMENT_SPEED, Clockware_Modifiers.SPEED_OBSIDIAN_CORE_2
                    )
            );

            // Apply all modifiers
            bodyDefinitions.forEach((item, attributes) ->
                    attributes.forEach((attribute, modifier) ->
                            event.setAttributeIf(event.onBody(item), attribute, modifier)
                    )
            );
        }

        //Arms Attributes
        {
            //Golem arm
            {

                //Maps the items with the attributes
                Map<Item, Map<Holder<Attribute>, Pair<AttributeModifier, AttributeModifier>>> armDefinitions = Map.of(
                        //Prototype
                        Clockware_Items.GOLEM_ARM_PROTOTYPE.get(), Map.of(
                                Attributes.ATTACK_DAMAGE, Pair.of(Clockware_Modifiers.STRENGTH_GOLEM_ARM_MAIN_0, Clockware_Modifiers.STRENGTH_GOLEM_ARM_OFFHAND_0),
                                Attributes.BLOCK_BREAK_SPEED, Pair.of(Clockware_Modifiers.BREAK_SPEED_GOLEM_ARM_MAIN_0, Clockware_Modifiers.BREAK_SPEED_GOLEM_ARM_OFFHAND_0),
                                Attributes.SUBMERGED_MINING_SPEED, Pair.of(Clockware_Modifiers.SUBMERGED_BREAK_SPEED_GOLEM_ARM_MAIN_0, Clockware_Modifiers.SUBMERGED_BREAK_SPEED_GOLEM_ARM_OFFHAND_0)
                        ),
                        //Refined
                        Clockware_Items.GOLEM_ARM_REFINED.get(), Map.of(
                                Attributes.ATTACK_DAMAGE, Pair.of(Clockware_Modifiers.STRENGTH_GOLEM_ARM_MAIN_1, Clockware_Modifiers.STRENGTH_GOLEM_ARM_OFFHAND_1),
                                Attributes.BLOCK_BREAK_SPEED, Pair.of(Clockware_Modifiers.BREAK_SPEED_GOLEM_ARM_MAIN_1, Clockware_Modifiers.BREAK_SPEED_GOLEM_ARM_OFFHAND_1),
                                Attributes.SUBMERGED_MINING_SPEED, Pair.of(Clockware_Modifiers.SUBMERGED_BREAK_SPEED_GOLEM_ARM_MAIN_1, Clockware_Modifiers.SUBMERGED_BREAK_SPEED_GOLEM_ARM_OFFHAND_1)
                        ),
                        //Masterwork
                        Clockware_Items.GOLEM_ARM_MASTERWORK.get(), Map.of(
                                Attributes.ATTACK_DAMAGE, Pair.of(Clockware_Modifiers.STRENGTH_GOLEM_ARM_MAIN_2, Clockware_Modifiers.STRENGTH_GOLEM_ARM_OFFHAND_2),
                                Attributes.BLOCK_BREAK_SPEED, Pair.of(Clockware_Modifiers.BREAK_SPEED_GOLEM_ARM_MAIN_2, Clockware_Modifiers.BREAK_SPEED_GOLEM_ARM_OFFHAND_2),
                                Attributes.SUBMERGED_MINING_SPEED, Pair.of(Clockware_Modifiers.SUBMERGED_BREAK_SPEED_GOLEM_ARM_MAIN_2, Clockware_Modifiers.SUBMERGED_BREAK_SPEED_GOLEM_ARM_OFFHAND_2)
                        )
                );

                // Apply all modifiers
                armDefinitions.forEach((item, attributes) ->
                        attributes.forEach((attribute, modifiers) ->
                                event.setAttribute_OffHand_MainHand(item, attribute, modifiers.getLeft(), modifiers.getRight())
                        )
                );
            }

            //Feline Hand
            {

                //Maps the items with the attributes
                Map<Item, Map<Holder<Attribute>, Pair<AttributeModifier, AttributeModifier>>> armDefinitions = Map.of(
                        //Prototype
                        Clockware_Items.FELINE_HAND_PROTOTYPE.get(), Map.of(
                                Attributes.BLOCK_INTERACTION_RANGE, Pair.of(Clockware_Modifiers.REACH_FELINE_HAND_MAIN_0, Clockware_Modifiers.REACH_FELINE_HAND_OFFHAND_0),
                                Attributes.ENTITY_INTERACTION_RANGE, Pair.of(Clockware_Modifiers.RANGE_FELINE_HAND_MAIN_0, Clockware_Modifiers.RANGE_FELINE_HAND_OFFHAND_0),
                                Attributes.ATTACK_SPEED, Pair.of(Clockware_Modifiers.SPEED_FELINE_HAND_MAIN_0, Clockware_Modifiers.SPEED_FELINE_HAND_OFFHAND_0)
                        )
                        ,
                        //Refined
                        Clockware_Items.FELINE_HAND_REFINED.get(), Map.of(
                                Attributes.BLOCK_INTERACTION_RANGE, Pair.of(Clockware_Modifiers.REACH_FELINE_HAND_MAIN_1, Clockware_Modifiers.REACH_FELINE_HAND_OFFHAND_1),
                                Attributes.ENTITY_INTERACTION_RANGE, Pair.of(Clockware_Modifiers.RANGE_FELINE_HAND_MAIN_1, Clockware_Modifiers.RANGE_FELINE_HAND_OFFHAND_1),
                                Attributes.ATTACK_SPEED, Pair.of(Clockware_Modifiers.SPEED_FELINE_HAND_MAIN_1, Clockware_Modifiers.SPEED_FELINE_HAND_OFFHAND_1)
                        ),
                        //Masterwork
                        Clockware_Items.FELINE_HAND_MASTERWORK.get(), Map.of(
                                Attributes.BLOCK_INTERACTION_RANGE, Pair.of(Clockware_Modifiers.REACH_FELINE_HAND_MAIN_2, Clockware_Modifiers.REACH_FELINE_HAND_OFFHAND_2),
                                Attributes.ENTITY_INTERACTION_RANGE, Pair.of(Clockware_Modifiers.RANGE_FELINE_HAND_MAIN_2, Clockware_Modifiers.RANGE_FELINE_HAND_OFFHAND_2),
                                Attributes.ATTACK_SPEED, Pair.of(Clockware_Modifiers.SPEED_FELINE_HAND_MAIN_2, Clockware_Modifiers.SPEED_FELINE_HAND_OFFHAND_2)
                        )
                );

                // Apply all modifiers
                armDefinitions.forEach((item, attributes) ->
                        attributes.forEach((attribute, modifiers) ->
                                event.setAttribute_OffHand_MainHand(item, attribute, modifiers.getLeft(), modifiers.getRight())
                        )
                );
            }

            //Reaper Blade
            {
                //OffHand Speed
                {
                    event.setAttributeIf(
                            event.onOffArm(Clockware_Items.REAPER_BLADE_PROTOTYPE.get()),
                            Attributes.ATTACK_SPEED,
                            Clockware_Modifiers.SPEED_REAPER_BLADE_OFFHAND_0
                    );

                    event.setAttributeIf(
                            event.onOffArm(Clockware_Items.REAPER_BLADE_REFINED.get()),
                            Attributes.ATTACK_SPEED,
                            Clockware_Modifiers.SPEED_REAPER_BLADE_OFFHAND_1
                    );

                    event.setAttributeIf(
                            event.onOffArm(Clockware_Items.REAPER_BLADE_MASTERWORK.get()),
                            Attributes.ATTACK_SPEED,
                            Clockware_Modifiers.SPEED_REAPER_BLADE_OFFHAND_2
                    );
                }

                //Main Hand damage
                {
                    boolean condition1=event.onMainArm(Clockware_Items.REAPER_BLADE_PROTOTYPE.get())
                            && event.clockwareOnMainArm().getItem() instanceof ClockwareItem clockwareItem
                            && clockwareItem.doSweep(event.clockwareOnMainArm(), event.getEntity(), ClockwareSlotType.getMainArmSlot(event.getEntity()));

                    boolean condition2=event.onMainArm(Clockware_Items.REAPER_BLADE_REFINED.get())
                            && event.clockwareOnMainArm().getItem() instanceof ClockwareItem clockwareItem
                            && clockwareItem.doSweep(event.clockwareOnMainArm(), event.getEntity(), ClockwareSlotType.getMainArmSlot(event.getEntity()));

                    boolean condition3=event.onMainArm(Clockware_Items.REAPER_BLADE_MASTERWORK.get())
                            && event.clockwareOnMainArm().getItem() instanceof ClockwareItem clockwareItem
                            && clockwareItem.doSweep(event.clockwareOnMainArm(), event.getEntity(), ClockwareSlotType.getMainArmSlot(event.getEntity()));

                    event.setAttributeIf(
                            condition1 || condition2 || condition3,
                            Attributes.ATTACK_SPEED,
                            Clockware_Modifiers.SPEED_REAPER_BLADE_MAIN
                    );

                    event.setAttributeIf(
                            condition1,
                            Attributes.ATTACK_DAMAGE,
                            Clockware_Modifiers.ATTACK_REAPER_BLADE_MAIN_0
                    );

                    event.setAttributeIf(
                            condition2,
                            Attributes.ATTACK_DAMAGE,
                            Clockware_Modifiers.ATTACK_REAPER_BLADE_MAIN_1
                    );

                    event.setAttributeIf(
                            condition3,
                            Attributes.ATTACK_DAMAGE,
                            Clockware_Modifiers.ATTACK_REAPER_BLADE_MAIN_2
                    );
                }
            }
        }

        //Legs Attributes
        {
            //Kinetic Actuators
            {
                //Maps the items with the attributes
                Map<Item, Map<Holder<Attribute>, Pair<AttributeModifier, AttributeModifier>>> legDefinitions = Map.of(
                        //Prototype
                        Clockware_Items.KINETIC_ACTUATOR_PROTOTYPE.get(), Map.of(
                                Attributes.MOVEMENT_SPEED, Pair.of(Clockware_Modifiers.SPEED_KINETIC_ACTUATORS_LEFT_0, Clockware_Modifiers.SPEED_KINETIC_ACTUATORS_RIGHT_0)
                        ),
                        //Refined
                        Clockware_Items.KINETIC_ACTUATOR_REFINED.get(), Map.of(
                                Attributes.MOVEMENT_SPEED, Pair.of(Clockware_Modifiers.SPEED_KINETIC_ACTUATORS_LEFT_1, Clockware_Modifiers.SPEED_KINETIC_ACTUATORS_RIGHT_1)
                        ),
                        //Masterwork
                        Clockware_Items.KINETIC_ACTUATOR_MASTERWORK.get(), Map.of(
                                Attributes.MOVEMENT_SPEED, Pair.of(Clockware_Modifiers.SPEED_KINETIC_ACTUATORS_LEFT_2, Clockware_Modifiers.SPEED_KINETIC_ACTUATORS_RIGHT_2)
                        )
                );

                // Apply all modifiers
                legDefinitions.forEach((item, attributes) ->
                        attributes.forEach((attribute, modifiers) ->
                                event.setAttribute_RightLeg_LeftLeg(item, attribute, modifiers.getRight(), modifiers.getLeft())
                        )
                );
            }

            //Jet Ankles
            {
                //Maps the items with the attributes
                Map<Item, Map<Holder<Attribute>, Pair<AttributeModifier, AttributeModifier>>> legDefinitions = Map.of(
                        //Prototype
                        Clockware_Items.JET_ANKLE_PROTOTYPE.get(), Map.of(
                                Attributes.SAFE_FALL_DISTANCE, Pair.of(Clockware_Modifiers.SAFE_FALL_JET_ANKLE_LEFT_0, Clockware_Modifiers.SAFE_FALL_JET_ANKLE_RIGHT_0),
                                Attributes.STEP_HEIGHT, Pair.of(Clockware_Modifiers.STEP_HEIGHT_JUMP_JET_ANKLE_LEFT_0, Clockware_Modifiers.STEP_HEIGHT_JUMP_JET_ANKLE_RIGHT_0)
                        ),
                        //Refined
                        Clockware_Items.JET_ANKLE_REFINED.get(), Map.of(
                                Attributes.SAFE_FALL_DISTANCE, Pair.of(Clockware_Modifiers.SAFE_FALL_JET_ANKLE_LEFT_1, Clockware_Modifiers.SAFE_FALL_JET_ANKLE_RIGHT_1),
                                Attributes.STEP_HEIGHT, Pair.of(Clockware_Modifiers.STEP_HEIGHT_JUMP_JET_ANKLE_LEFT_1, Clockware_Modifiers.STEP_HEIGHT_JUMP_JET_ANKLE_RIGHT_1)
                        ),
                        //Masterwork
                        Clockware_Items.JET_ANKLE_MASTERWORK.get(), Map.of(
                                Attributes.SAFE_FALL_DISTANCE, Pair.of(Clockware_Modifiers.SAFE_FALL_JET_ANKLE_LEFT_2, Clockware_Modifiers.SAFE_FALL_JET_ANKLE_RIGHT_2),
                                Attributes.STEP_HEIGHT, Pair.of(Clockware_Modifiers.STEP_HEIGHT_JUMP_JET_ANKLE_LEFT_2, Clockware_Modifiers.STEP_HEIGHT_JUMP_JET_ANKLE_RIGHT_2)
                        )
                );

                // Apply all modifiers
                legDefinitions.forEach((item, attributes) ->
                        attributes.forEach((attribute, modifiers) ->
                                event.setAttribute_RightLeg_LeftLeg(item, attribute, modifiers.getRight(), modifiers.getLeft())
                        )
                );
            }
        }

        //Auto-Retracts Reaper Blade
        {
            if(event.onMainArm("reaper_blade") && !event.getEntity().getMainHandItem().isEmpty()){

                ItemStack clockwareOnMainArm= event.clockwareOnMainArm();
                var bladeOutMain= clockwareOnMainArm.get(Clockware_Components.BLADE_OUT);

                if(bladeOutMain!=null){
                    //Retracts
                    if(bladeOutMain){
                        boolean mainRetracted= ReaperBladeArm.toggleBlade(event.getEntity(), SoundEvents.PISTON_CONTRACT, clockwareOnMainArm, false);

                        if(mainRetracted && event.onOffArm("reaper_blade")){
                            ItemStack clockwareOnOffArm= event.clockwareOnOffArm();
                            var bladeOutOff= clockwareOnOffArm.get(Clockware_Components.BLADE_OUT);

                            if(bladeOutOff!=null)
                                ReaperBladeArm.toggleBlade(event.getEntity(), SoundEvents.PISTON_CONTRACT, clockwareOnOffArm, false);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void clockwareDoubleJump(final ClockwareDoubleJumpEvent event){

        if(event.onBothLegs("kinetic_actuator")){
            LivingEntity entity = event.getEntity();

            if (entity instanceof Player player && !player.getCooldowns().isOnCooldown(event.getClockwareBySlot(ClockwareSlotType.RIGHT_LEG).getItem())
                    && player.getFoodData().getFoodLevel()>2
                    && !player.isCreative()
                    && !player.isInLiquid()
                    && !player.isFallFlying()) {
                Vec3 lookVec = player.getLookAngle();

                ItemStack leftKineticActuator  = event.getClockwareBySlot(ClockwareSlotType.LEFT_LEG);
                ItemStack rightKineticActuator = event.getClockwareBySlot(ClockwareSlotType.RIGHT_LEG);

                if(leftKineticActuator.getItem() instanceof ClockwareItem leftClockwareItem && rightKineticActuator.getItem() instanceof ClockwareItem rightClockwareItem){
                    float dashSpeed = 1.0f + (leftClockwareItem.getLevel()*0.5f) + (rightClockwareItem.getLevel()*0.5f);
                    int cooldown = 60;

                    //Being invulnerable by 15 ticks to dash across entities
                    player.invulnerableTime=15;

                    player.setDeltaMovement(
                            lookVec.x * dashSpeed,
                            0,
                            lookVec.z * dashSpeed
                    );
                    player.hurtMarked = true;

                    //Resets fall distance, so you can avoid fall damage
                    player.resetFallDistance();

                    //Jump particles
                    if(player.level() instanceof ServerLevel serverLevel)
                        (serverLevel).sendParticles(ParticleTypes.POOF,
                            player.getX(), player.getY()+ player.getBbHeight() * 0.5,
                            player.getZ(), 16, 0.0, 0.0, 0.0, 0.1);

                    player.level().playSound(null, player.blockPosition(),
                            SoundEvents.PLAYER_BIG_FALL, SoundSource.PLAYERS,
                            1.0F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);

                    //Add cooldowns
                    player.getCooldowns().addCooldown(leftKineticActuator.getItem(), cooldown);
                    player.getCooldowns().addCooldown(rightKineticActuator.getItem(), cooldown);

                    //Gives player Hunger
                    if (!player.isCreative()) {
                        player.getFoodData().addExhaustion(1.0f);
                    }
                }

            }
        }

        if(event.onBothLegs("jet_ankle")){
            LivingEntity entity = event.getEntity();

            if (entity instanceof Player player && !player.getCooldowns().isOnCooldown(event.getClockwareBySlot(ClockwareSlotType.RIGHT_LEG).getItem())
                    && player.getFoodData().getFoodLevel()>2
                    && !player.isCreative()
                    && !player.isInLiquid()
                    && !player.isFallFlying()) {

                ItemStack leftKineticActuator  = event.getClockwareBySlot(ClockwareSlotType.LEFT_LEG);
                ItemStack rightKineticActuator = event.getClockwareBySlot(ClockwareSlotType.RIGHT_LEG);

                if(leftKineticActuator.getItem() instanceof ClockwareItem leftClockwareItem && rightKineticActuator.getItem() instanceof ClockwareItem rightClockwareItem){
                    float verticalImpulse = 0.50f + (leftClockwareItem.getLevel()*0.10f) + (rightClockwareItem.getLevel()*0.10f);
                    int cooldown = 40;

                    player.setDeltaMovement(
                            0,
                            verticalImpulse,
                            0
                    );
                    player.hurtMarked = true;

                    //Resets fall distance, so you can avoid fall damage
                    player.resetFallDistance();

                    //Jump particles
                    if(player.level() instanceof ServerLevel serverLevel)
                        (serverLevel).sendParticles(ParticleTypes.SMALL_GUST,
                                player.getX(), player.getY()+ player.getBbHeight() * 0.5,
                                player.getZ(), 32,  0.2, 0.2, 0.2, 0.8);

                    player.level().playSound(null, player.blockPosition(),
                            SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.PLAYERS,
                            1.0F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);

                    //Add cooldowns
                    player.getCooldowns().addCooldown(leftKineticActuator.getItem(), cooldown);
                    player.getCooldowns().addCooldown(rightKineticActuator.getItem(), cooldown);

                    //Gives player Hunger
                    if (!player.isCreative()) {
                        player.getFoodData().addExhaustion(0.5f);
                    }
                }

            }
        }

        if(event.onBothLegs("hydrothruster")){
            LivingEntity entity = event.getEntity();

            if (entity instanceof Player player && !player.getCooldowns().isOnCooldown(event.getClockwareBySlot(ClockwareSlotType.RIGHT_LEG).getItem())
                    && !player.isCreative()
                    && player.isInLiquid()
                    && !player.isFallFlying()) {

                ItemStack leftHydrothruster  = event.getClockwareBySlot(ClockwareSlotType.LEFT_LEG);
                ItemStack rightHydrothruster = event.getClockwareBySlot(ClockwareSlotType.RIGHT_LEG);

                if(leftHydrothruster.getItem() instanceof ClockwareItem leftClockwareItem && rightHydrothruster.getItem() instanceof ClockwareItem rightClockwareItem){
                    int cooldown = 40;

                    //Being invulnerable by 15 ticks to dash across entities
                    player.invulnerableTime=15;

                    float intensity = 0.50f + (leftClockwareItem.getLevel()*0.50f) + (rightClockwareItem.getLevel()*0.50f);
                    float damage = 2.0f + (leftClockwareItem.getLevel()*2.0f) + (rightClockwareItem.getLevel()*2.0f);
                    float f7 = player.getYRot();
                    float f1 = player.getXRot();
                    float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
                    float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
                    float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
                    float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
                    f2 *= intensity / f5;
                    f3 *= intensity / f5;
                    f4 *= intensity / f5;
                    player.push(f2, f3, f4);
                    player.startAutoSpinAttack(20, damage, ItemStack.EMPTY);
                    if (player.onGround()) {
                        player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                    }

                    player.level().playSound(null, player.blockPosition(),
                            SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE, SoundSource.PLAYERS,
                            1.0F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);

                    player.hurtMarked = true;

                    //Add cooldowns
                    player.getCooldowns().addCooldown(leftHydrothruster.getItem(), cooldown);
                    player.getCooldowns().addCooldown(rightHydrothruster.getItem(), cooldown);

                    //Gives player Hunger
                    if (!player.isCreative()) {
                        player.getFoodData().addExhaustion(0.2f);
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public static void clockwareLeftClickRelease(final ClockwareLeftClickEvent.Release event){
        //Reaper Blade
        {
            if(event.getUseTime()>10){
                if(event.onMainArm("reaper_blade") && event.getEntity().getMainHandItem().isEmpty()){

                    ItemStack clockwareOnMainArm= event.clockwareOnMainArm();
                    var bladeOutMain= clockwareOnMainArm.get(Clockware_Components.BLADE_OUT);

                    if(bladeOutMain!=null){
                        //Extends
                        if(!bladeOutMain){
                           boolean mainExtended= ReaperBladeArm.toggleBlade(event.getEntity(), SoundEvents.PISTON_EXTEND, clockwareOnMainArm, true);

                           if(mainExtended && event.onOffArm("reaper_blade") && event.getEntity().getOffhandItem().isEmpty()){
                               ItemStack clockwareOnOffArm= event.clockwareOnOffArm();
                               var bladeOutOff= clockwareOnOffArm.get(Clockware_Components.BLADE_OUT);

                               if(bladeOutOff!=null)
                                   ReaperBladeArm.toggleBlade(event.getEntity(), SoundEvents.PISTON_EXTEND, clockwareOnOffArm, true);
                           }
                        }
                        //Retracts
                        else {
                            boolean mainRetracted= ReaperBladeArm.toggleBlade(event.getEntity(), SoundEvents.PISTON_CONTRACT, clockwareOnMainArm, false);

                            if(mainRetracted && event.onOffArm("reaper_blade")){
                                ItemStack clockwareOnOffArm= event.clockwareOnOffArm();
                                var bladeOutOff= clockwareOnOffArm.get(Clockware_Components.BLADE_OUT);

                                if(bladeOutOff!=null)
                                    ReaperBladeArm.toggleBlade(event.getEntity(), SoundEvents.PISTON_CONTRACT, clockwareOnOffArm, false);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void clockwareRightClickRelease(final ClockwareRightClickEvent.Release event){

        // Reaper Blade Lunge
        {
            if (Clockware_Util.hasDualwieldClockware(event.getEntity())
                    && event.onMainArm("reaper_blade")
                    && event.onOffArm("reaper_blade")) {

                if (event.getUseTime() > 10) {
                    LivingEntity entity = event.getEntity();

                    if (entity instanceof Player player && !player.getCooldowns().isOnCooldown(player.clockware$getClockwareOnMainHand().getItem()) && player.getFoodData().getFoodLevel()>2) {
                        Vec3 lookVec = player.getLookAngle();

                        ItemStack mainReaperBlade = player.clockware$getClockwareOnMainHand();
                        ItemStack offReaperBlade = player.clockware$getClockwareOnOffHand();

                        if(mainReaperBlade.getItem() instanceof ClockwareItem mainClockwareItem && offReaperBlade.getItem() instanceof ClockwareItem offClockwareItem){
                            float lungeSpeed    = 1.0f + (mainClockwareItem.getLevel()*0.25f) + (offClockwareItem.getLevel()*0.25f);
                            float verticalBoost = 0.2f + (mainClockwareItem.getLevel()*0.10f) + (offClockwareItem.getLevel()*0.10f);
                            int cooldown = 60;

                            //Being invulnerable by 15 ticks to being able to hit directly the enemies
                            player.invulnerableTime=15;
                            player.setDeltaMovement(
                                    lookVec.x * lungeSpeed,
                                    lookVec.y * verticalBoost,
                                    lookVec.z * lungeSpeed
                            );
                            player.hurtMarked = true;
                            //Swing animation
                            player.swing(InteractionHand.MAIN_HAND, true);

                            //Damage
                            if(player instanceof ServerPlayer serverPlayer){
                                serverPlayer.clockware$setLungeTicks(20);
                            }


                            //Resets fall distance, so you can avoid fall damage (Like Cyberpunk, hell yeah)
                            player.resetFallDistance();

                            player.level().playSound(null, player.blockPosition(),
                                    SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS,
                                    1.0F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);

                            //Add cooldowns
                            player.getCooldowns().addCooldown(event.getEntity().clockware$getClockwareOnMainHand().getItem(), cooldown);
                            player.getCooldowns().addCooldown(event.getEntity().clockware$getClockwareOnMainHand().getItem(), cooldown);

                            //Gives player Hunger
                            if (!player.isCreative()) {
                                player.getFoodData().addExhaustion(2.0f);
                            }
                        }

                    }
                }
            }
        }


        //Projectile Launcher
        {
            boolean reloaded1;
            boolean reloaded2=false;
            boolean reloaded3=false;
            boolean reloaded4=false;
            boolean reloaded5=false;


            //Made in a chained way to take into account any arm combination and never fire accidentally an arm
            reloaded1 = projectileLauncherLogic(event, Clockware_Items.PROJECTILE_LAUNCHER_MASTERWORK.get(), HumanoidArm.RIGHT, 3, 0, 20);
            if (!reloaded1) reloaded2=  projectileLauncherLogic(event, Clockware_Items.PROJECTILE_LAUNCHER_MASTERWORK.get(), HumanoidArm.LEFT, 3, 0, 20);


            if(!reloaded1 && !reloaded2) reloaded3 = projectileLauncherLogic(event, Clockware_Items.PROJECTILE_LAUNCHER_REFINED.get(), HumanoidArm.RIGHT, 2, 1, 30);

            if (!reloaded1 && !reloaded2 && !reloaded3) reloaded4= projectileLauncherLogic(event, Clockware_Items.PROJECTILE_LAUNCHER_REFINED.get(), HumanoidArm.LEFT, 2, 1, 30);


            if (!reloaded1 && !reloaded2 && !reloaded3 && !reloaded4) reloaded5 = projectileLauncherLogic(event, Clockware_Items.PROJECTILE_LAUNCHER_PROTOTYPE.get(), HumanoidArm.RIGHT, 1, 2, 40);

            if (!reloaded1 && !reloaded2 && !reloaded3 && !reloaded4 && !reloaded5) projectileLauncherLogic(event, Clockware_Items.PROJECTILE_LAUNCHER_PROTOTYPE.get(), HumanoidArm.LEFT, 1, 2, 40);
        }
    }

    private static boolean projectileLauncherLogic(ClockwareRightClickEvent.Release event, Item launcherItem, HumanoidArm arm, int shootSpeed, int shootInaccuracy, int useTime){
        ClockwareSlotType slot = arm.equals(HumanoidArm.RIGHT)? ClockwareSlotType.RIGHT_ARM: ClockwareSlotType.LEFT_ARM;

        //Reload logic
        if(event.isClockwareOnSlotSameAs(slot, launcherItem) && event.getUseTime()>=useTime
                //Only while shifting
                && event.getEntity().isShiftKeyDown()
                //If is projectile on the other hand
                && event.getEntity().getItemInHand(event.getEntity().getMainArm().equals(arm)? InteractionHand.OFF_HAND:  InteractionHand.MAIN_HAND).getItem() instanceof ProjectileItem){

            ItemStack launcher = event.getClockwareBySlot(slot);

            ProjectileArmStorage storage =  launcher.get(Clockware_Components.PROJECTILE_ARM_STORAGE);
            ItemStack newAmmo = event.getEntity().getItemInHand(event.getEntity().getMainArm().equals(arm)? InteractionHand.OFF_HAND:  InteractionHand.MAIN_HAND);

            if(storage!=null){
                ProjectileArmStorage.Mutable storageMutable = new ProjectileArmStorage.Mutable(storage);

                int inserted = storageMutable.tryInsert(newAmmo.copy());
                if(inserted>0){

                    storageMutable.updateStacks();

                    newAmmo.shrink(inserted);

                    storageMutable.updateStacks();

                    event.getEntity().level().playSound(null, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 0.8F, 0.8F + event.getEntity().level().getRandom().nextFloat() * 0.4F);

                    launcher.set(Clockware_Components.PROJECTILE_ARM_STORAGE, storageMutable.toImmutable());

                    if(event.getEntity() instanceof ServerPlayer serverPlayer)
                        Clockware_Util.syncClockware(serverPlayer);

                    //Avoids shooting
                    return true;
                }
            }
        }

        //Shoot logic
        if(event.isClockwareOnSlotSameAs(slot, launcherItem) && event.getUseTime()>= useTime
                //Only if empty
                && event.getEntity().getItemInHand(event.getEntity().getMainArm().equals(arm)? InteractionHand.MAIN_HAND:  InteractionHand.OFF_HAND).isEmpty()){

            ItemStack launcher = event.getClockwareBySlot(slot);
            ProjectileArmStorage storage =  launcher.get(Clockware_Components.PROJECTILE_ARM_STORAGE);

            if(storage!=null){
                ProjectileArmStorage.Mutable storageMutable = new ProjectileArmStorage.Mutable(storage);

                if(!storageMutable.getItems().isEmpty()){
                    ItemStack ammo = storageMutable.getItems().getFirst();

                    ProjectileLauncherArm.performShooting(event.getEntity().level(), event.getEntity(), arm, ammo.copy(),shootSpeed, shootInaccuracy);

                    ammo.shrink(1);
                    storageMutable.updateStacks();

                    launcher.set(Clockware_Components.PROJECTILE_ARM_STORAGE, storageMutable.toImmutable());
                }
                else {

                    ProjectileLauncherArm.unsuccessfulShoot(event.getEntity().level(), event.getEntity(), arm);
                }
            }

            if(event.getEntity() instanceof ServerPlayer serverPlayer)
                Clockware_Util.syncClockware(serverPlayer);
        }

        return false;
    }

    @SubscribeEvent
    public static void clockwareEnableSweep(final SweepAttackEvent event){

        for (ClockwareSlotType slotType: ClockwareSlotType.values()){

            ItemStack itemStack= event.getEntity().clockware$getClockwareBySlot(slotType);

            if(itemStack.getItem() instanceof ClockwareItem clockwareItem
                    && clockwareItem.doSweep(itemStack, event.getEntity(), ClockwareSlotType.getMainArmSlot(event.getEntity()))){

                //Vanilla check, to not sweep when crit
                boolean flag4 = event.getEntity().getAttackStrengthScale(0.5F) > 0.9F;
                boolean isCrit = flag4
                        && event.getEntity().fallDistance > 0.0F
                        && !event.getEntity().onGround()
                        && !event.getEntity().onClimbable()
                        && !event.getEntity().isInWater()
                        && !event.getEntity().hasEffect(MobEffects.BLINDNESS)
                        && !event.getEntity().isPassenger()
                        && event.getTarget() instanceof LivingEntity
                        && !event.getEntity().isSprinting();

                if(!isCrit)
                    event.setSweeping(true);
            }
        }
    }
}
