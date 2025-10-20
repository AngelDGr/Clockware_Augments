package mors.clockware;

import mors.clockware.networking.HandlePackets;
import mors.clockware.networking.packets.*;
import mors.clockware.networking.packets.buttons.DoubleJumpClockware;
import mors.clockware.networking.packets.buttons.StartClockwareClick;
import mors.clockware.networking.packets.buttons.StopClockwareRightClick;
import mors.clockware.registry.*;
import mors.clockware.utils.Clockware_Util;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Clockware_Main.MOD_ID)
@Mod(Clockware_Main.MOD_ID)
public class Clockware_Main {
    public static final String MOD_ID = "clockware";

    public Clockware_Main(IEventBus modEventBus, ModContainer modContainer) {
        //Blocks
        Clockware_Blocks.init(); Clockware_Registries.BLOCKS.register(modEventBus);

        //Items
        Clockware_Items.init(); Clockware_Registries.ITEMS.register(modEventBus); Clockware_Registries.LOOT_FUNCTIONS.register(modEventBus);
        Clockware_Items.initGroups(); Clockware_Registries.CREATIVE_MODE_TABS.register(modEventBus);
        Clockware_Components.init(); Clockware_Registries.DATA_COMPONENTS.register(modEventBus);

        //Screens
        Clockware_Screens.init(); Clockware_Registries.MENUS.register(modEventBus);

        //Advancements
        Clockware_Advancements.init(); Clockware_Registries.CRITERIA.register(modEventBus);

        //Villagers
        Clockware_POI.init(); Clockware_Registries.POI_TYPES.register(modEventBus);
        Clockware_Villagers.init(); Clockware_Registries.VILLAGER_PROFESSIONS.register(modEventBus);
        Clockware_Villagers.Trades.init();
        Clockware_Villagers.Buildings.init();
    }

    @SubscribeEvent
    public static void registerCommonEvent(final FMLCommonSetupEvent event){}

    @SubscribeEvent
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar mainThread = event.registrar("1");

        final PayloadRegistrar networkThread = event.registrar("1").executesOn(HandlerThread.NETWORK);

        mainThread.playToServer(
                OpenRipperdocPacket.TYPE,
                OpenRipperdocPacket.STREAM_CODEC,
                HandlePackets.OnServer::handleOpenRipperdocPacket
        );

        mainThread.playToClient(
                SyncVillagerProfession.TYPE,
                SyncVillagerProfession.STREAM_CODEC,
                HandlePackets.OnClient::handleSyncVillagerProfessionPacket
        );

        mainThread.playToClient(
                SyncClockwareItemsPacket.TYPE,
                SyncClockwareItemsPacket.STREAM_CODEC,
                HandlePackets.OnClient::handleSyncClockwareItemsPacket
        );

        mainThread.playToServer(
                SyncClockwareButtonPacket.TYPE,
                SyncClockwareButtonPacket.STREAM_CODEC,
                HandlePackets.OnServer::handleSyncClockwareButtonPacket
        );

        mainThread.playToServer(
                StartClockwareClick.TYPE,
                StartClockwareClick.STREAM_CODEC,
                HandlePackets.OnServer::handleStartClockwareClick
        );

        mainThread.playToServer(
                StopClockwareRightClick.TYPE,
                StopClockwareRightClick.STREAM_CODEC,
                HandlePackets.OnServer::handleStopClockwareClick
        );

        mainThread.playToServer(
                DoubleJumpClockware.TYPE,
                DoubleJumpClockware.STREAM_CODEC,
                HandlePackets.OnServer::handleDoubleJumpClockware
        );
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Clockware_Util.syncClockware(event.getEntity());

        for(Player player: event.getEntity().level().players())
            Clockware_Util.syncClockware(player);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Clockware_Util.syncClockware(event.getEntity());

        for(Player player: event.getEntity().level().players())
            Clockware_Util.syncClockware(player);
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Clockware_Util.syncClockware(event.getEntity());

        for(Player player: event.getEntity().level().players())
            Clockware_Util.syncClockware(player);
    }

    @SubscribeEvent
    public static void onPlayerTrack(PlayerEvent.StartTracking event) {
        Clockware_Util.syncClockware(event.getEntity());

        Clockware_Util.syncClockware(event.getTarget());
    }
}
