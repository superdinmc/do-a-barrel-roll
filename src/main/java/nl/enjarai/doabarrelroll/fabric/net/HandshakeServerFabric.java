package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.net.HandshakeServer;
import nl.enjarai.doabarrelroll.net.RollSyncServer;

public class HandshakeServerFabric {
    public static void init() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, DoABarrelRoll.HANDSHAKE_CHANNEL, (server1, player, handler1, buf, responseSender) -> {
                if (DoABarrelRoll.HANDSHAKE_SERVER.clientReplied(player, buf) == HandshakeServer.HandshakeState.ACCEPTED) {
                    RollSyncServer.startListening(handler1);
                }
            });

            ServerPlayNetworking.send(handler.getPlayer(), DoABarrelRoll.HANDSHAKE_CHANNEL,
                    DoABarrelRoll.HANDSHAKE_SERVER.getConfigSyncBuf(handler.getPlayer()));

            DoABarrelRoll.HANDSHAKE_SERVER.configSentToClient(handler.getPlayer());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            DoABarrelRoll.HANDSHAKE_SERVER.playerDisconnected(handler.getPlayer());
        });

        ServerTickEvents.END_SERVER_TICK.register(DoABarrelRoll.HANDSHAKE_SERVER::tick);
    }
}
