package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.net.HandshakeServer;

public class HandshakeServerFabric {
    public static void init() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.send(handler.getPlayer(), DoABarrelRoll.SYNC_CHANNEL, HandshakeServer.getConfigSyncBuf(handler.getPlayer()));
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            HandshakeServer.playerDisconnected(handler.getPlayer());
        });

        ServerPlayNetworking.registerGlobalReceiver(DoABarrelRoll.SYNC_CHANNEL, (server, player, handler, buf, responseSender) -> {
            HandshakeServer.clientReplied(player, buf);
        });
    }
}
