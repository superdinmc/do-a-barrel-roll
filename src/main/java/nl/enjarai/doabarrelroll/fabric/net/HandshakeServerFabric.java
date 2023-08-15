package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.event.ServerEvents;
import nl.enjarai.doabarrelroll.net.HandshakeServer;
import nl.enjarai.doabarrelroll.net.RollSyncServer;

public class HandshakeServerFabric {
    public static void init() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, DoABarrelRoll.HANDSHAKE_CHANNEL, (server1, player, handler1, buf, responseSender) -> {
                var reply = DoABarrelRoll.HANDSHAKE_SERVER.clientReplied(handler1, buf);
                if (reply == HandshakeServer.HandshakeState.ACCEPTED) {
                    RollSyncServer.startListening(handler1);
                    ServerConfigUpdaterFabric.startListening(handler1);
                } else if (reply == HandshakeServer.HandshakeState.RESEND) {
                    // Resending can happen when the client has a different protocol version than expected.
                    sendHandshake(player);
                }
            });

            // The initial handshake is sent in the CommandManagerMixin.
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            DoABarrelRoll.HANDSHAKE_SERVER.playerDisconnected(handler);
        });

        ServerTickEvents.END_SERVER_TICK.register(DoABarrelRoll.HANDSHAKE_SERVER::tick);

        ServerEvents.SERVER_CONFIG_UPDATE.register((server, config) -> {
            for (var player : server.getPlayerManager().getPlayerList()) {
                sendHandshake(player);
            }
        });
    }

    public static void sendHandshake(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, DoABarrelRoll.HANDSHAKE_CHANNEL,
                DoABarrelRoll.HANDSHAKE_SERVER.getConfigSyncBuf(player.networkHandler));

        DoABarrelRoll.HANDSHAKE_SERVER.configSentToClient(player.networkHandler);
    }
}
