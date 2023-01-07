package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.net.HandshakeServer;

public class HandshakeServerFabric {
    public static void init() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, DoABarrelRoll.SYNC_CHANNEL, (server1, player, handler1, buf, responseSender) -> {
                if (DoABarrelRoll.handshakeServer.clientReplied(player, buf) == HandshakeServer.HandshakeState.ACCEPTED) {
                    RollSyncServer.startListening(handler1);
                }
            });

            ServerPlayNetworking.send(handler.getPlayer(), DoABarrelRoll.SYNC_CHANNEL,
                    DoABarrelRoll.handshakeServer.getConfigSyncBuf(handler.getPlayer()));
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            DoABarrelRoll.handshakeServer.playerDisconnected(handler.getPlayer());
        });
    }
}
