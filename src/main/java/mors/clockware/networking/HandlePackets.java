package mors.clockware.networking;

import mors.clockware.client.screen.ClockwareMenu;
import mors.clockware.client.screen.ClockwareMenuProvider;
import mors.clockware.event.ClockwareDoubleJumpEvent;
import mors.clockware.event.ClockwareLeftClickEvent;
import mors.clockware.event.ClockwareRightClickEvent;
import mors.clockware.item.ClockwareSlotType;
import mors.clockware.networking.packets.*;
import mors.clockware.networking.packets.buttons.DoubleJumpClockware;
import mors.clockware.networking.packets.buttons.StartClockwareClick;
import mors.clockware.networking.packets.buttons.StopClockwareRightClick;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class HandlePackets {

    public static class OnClient {

        public static void handleSyncVillagerProfessionPacket(final SyncVillagerProfession data, final IPayloadContext context) {
            if (context.player().containerMenu instanceof MerchantMenu merchantMenu)
                merchantMenu.clockware$setProfession(data.profession());
        }

        public static void handleSyncClockwareItemsPacket(final SyncClockwareItemsPacket data, final IPayloadContext context) {

            if(context.player().level().getEntity(data.entityId()) instanceof LivingEntity livingEntity)
                for (int i = 0; i < data.clockwareItems().size(); i++) {
                    livingEntity.clockware$getClockwareInventory().set(i, data.clockwareItems().get(i).copy());
                }
        }

    }

    public static class OnServer {

        public static void handleOpenRipperdocPacket(final OpenRipperdocPacket data, final IPayloadContext context) {
            var villager = context.player().level()
                    .getEntitiesOfClass(Villager.class, context.player().getBoundingBox().inflate(5),
                            v -> v.getTradingPlayer() == context.player())
                    .stream().findFirst().orElse(null);

            if(villager!=null){

                context.player().openMenu(new ClockwareMenuProvider(
                        (x, inventory, player) ->
                                new ClockwareMenu(x, inventory, villager),
                        Component.translatable("gui.clockware.ripperdoc.installation"), villager.getId()
                ));

                //To keep the trading player
                villager.setTradingPlayer(context.player());
            }
        }

        public static void handleSyncClockwareButtonPacket(final SyncClockwareButtonPacket data, final IPayloadContext context) {
            if(context.player().containerMenu instanceof ClockwareMenu menu){
                menu.setClockwareBySlot(ClockwareSlotType.values()[data.clockwareSlot()], data.clockware());
            }
        }

        public static void handleDoubleJumpClockware(final DoubleJumpClockware data, final IPayloadContext context) {
            if(context.player().level().getEntity(data.entityId()) instanceof LivingEntity livingEntity) {
                NeoForge.EVENT_BUS.post(new ClockwareDoubleJumpEvent(livingEntity, livingEntity.clockware$getClockwareInventory()));
            }
        }

        public static void handleStartClockwareClick(final StartClockwareClick data, final IPayloadContext context) {
            if(context.player().level().getEntity(data.entityId()) instanceof Player livingEntity){
                if(data.rightClick()){
                    livingEntity.clockware$setIsRightClicking(true);
                    NeoForge.EVENT_BUS.post(new ClockwareRightClickEvent.Start(livingEntity, livingEntity.clockware$getClockwareInventory()));
                }
                else
                    NeoForge.EVENT_BUS.post(new ClockwareLeftClickEvent.Start(livingEntity, livingEntity.clockware$getClockwareInventory()));
            }
        }

        public static void handleStopClockwareClick(final StopClockwareRightClick data, final IPayloadContext context) {
            if(context.player().level().getEntity(data.entityId()) instanceof Player livingEntity) {
                if(data.rightClick()){
                    livingEntity.clockware$setIsRightClicking(false);
                    NeoForge.EVENT_BUS.post(new ClockwareRightClickEvent.Release(livingEntity, livingEntity.clockware$getClockwareInventory(), data.useTime()));
                }
                else
                    NeoForge.EVENT_BUS.post(new ClockwareLeftClickEvent.Release(livingEntity, livingEntity.clockware$getClockwareInventory(), data.useTime()));
            }


        }
    }
}