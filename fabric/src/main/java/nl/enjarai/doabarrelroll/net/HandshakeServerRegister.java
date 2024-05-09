package nl.enjarai.doabarrelroll.net;

import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.event.ServerEvents;
import nl.enjarai.doabarrelroll.net.HandshakeServer;
import nl.enjarai.doabarrelroll.platform.Services;

public class HandshakeServerRegister {
    public static void init() {
        Services.SERVER_NET.registerListener(DoABarrelRoll.HANDSHAKE_CHANNEL, (handler, buf, responseSender) -> {
            var reply = DoABarrelRoll.HANDSHAKE_SERVER.clientReplied(handler, buf);
            if (reply == HandshakeServer.HandshakeState.RESEND) {
                // Resending can happen when the client has a different protocol version than expected.
                sendHandshake(handler.getPlayer());
            }
        });
        // The initial handshake is sent in the CommandManagerMixin.

        ServerEvents.SERVER_CONFIG_UPDATE.register((server, config) -> {
            for (var player : server.getPlayerManager().getPlayerList()) {
                sendHandshake(player);
            }
        });
    }

    public static void sendHandshake(ServerPlayerEntity player) {
        Services.SERVER_NET.sendPacket(player.networkHandler, DoABarrelRoll.HANDSHAKE_CHANNEL,
                DoABarrelRoll.HANDSHAKE_SERVER.getConfigSyncBuf(player.networkHandler));

        DoABarrelRoll.HANDSHAKE_SERVER.configSentToClient(player.networkHandler);
    }
}
