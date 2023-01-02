package nl.enjarai.doabarrelroll.fabric.server;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.server.ConfigSyncServer;

public class FabricConfigSyncServer {
    public static void init() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.send(handler.getPlayer(), DoABarrelRoll.SYNC_CHANNEL, ConfigSyncServer.getConfigSyncBuf(handler.getPlayer()));
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ConfigSyncServer.playerDisconnected(handler.getPlayer());
        });

        ServerPlayNetworking.registerGlobalReceiver(DoABarrelRoll.SYNC_CHANNEL, (server, player, handler, buf, responseSender) -> {
            ConfigSyncServer.clientReplied(player, buf);
        });
    }
}
