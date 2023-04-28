package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.net.HandshakeServer;

public class HandshakeServerFabric {
    public static void init() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, DoABarrelRoll.SYNC_CHANNEL, (server1, player, handler1, buf, responseSender) -> {
                if (DoABarrelRoll.HANDSHAKE_SERVER.clientReplied(player, buf) == HandshakeServer.HandshakeState.ACCEPTED) {
                    RollSyncServer.startListening(handler1);
                }
            });

            ServerPlayNetworking.send(handler.player, DoABarrelRoll.SYNC_CHANNEL,
                    DoABarrelRoll.HANDSHAKE_SERVER.getConfigSyncBuf(handler.player));

            DoABarrelRoll.HANDSHAKE_SERVER.configSentToClient(handler.player);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            DoABarrelRoll.HANDSHAKE_SERVER.playerDisconnected(handler.player);
        });

        ServerTickEvents.END_SERVER_TICK.register(DoABarrelRoll.HANDSHAKE_SERVER::tick);
    }
}
