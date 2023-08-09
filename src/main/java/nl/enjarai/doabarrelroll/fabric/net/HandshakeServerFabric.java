package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.event.ServerEvents;
import nl.enjarai.doabarrelroll.net.HandshakeServer;
import nl.enjarai.doabarrelroll.net.RollSyncServer;

public class HandshakeServerFabric {
    public static void init() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, DoABarrelRoll.HANDSHAKE_CHANNEL, (server1, player, handler1, buf, responseSender) -> {
                if (DoABarrelRoll.HANDSHAKE_SERVER.clientReplied(handler1, buf) == HandshakeServer.HandshakeState.ACCEPTED) {
                    RollSyncServer.startListening(handler1);
                    ServerConfigUpdaterFabric.startListening(handler1);
                }
            });

            ServerPlayNetworking.send(handler.getPlayer(), DoABarrelRoll.HANDSHAKE_CHANNEL,
                    DoABarrelRoll.HANDSHAKE_SERVER.getConfigSyncBuf(handler));

            DoABarrelRoll.HANDSHAKE_SERVER.configSentToClient(handler);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            DoABarrelRoll.HANDSHAKE_SERVER.playerDisconnected(handler);
        });

        ServerTickEvents.END_SERVER_TICK.register(DoABarrelRoll.HANDSHAKE_SERVER::tick);

        ServerEvents.SERVER_CONFIG_UPDATE.register((server, config) -> {
            for (var player : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, DoABarrelRoll.HANDSHAKE_CHANNEL,
                        DoABarrelRoll.HANDSHAKE_SERVER.getConfigSyncBuf(player.networkHandler));

                DoABarrelRoll.HANDSHAKE_SERVER.configSentToClient(player.networkHandler);
            }
        });
    }
}
