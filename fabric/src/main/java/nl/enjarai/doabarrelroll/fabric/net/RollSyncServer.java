package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.fabric.data.Components;
import nl.enjarai.doabarrelroll.api.net.HandshakeServer;

public class RollSyncServer {
    public static void startListening(ServerPlayNetworkHandler handler) {
        ServerPlayNetworking.registerReceiver(handler, DoABarrelRoll.ROLL_CHANNEL, (server, player, handler1, buf, responseSender) -> {
            Components.ROLL.get(player).setRoll(buf.readDouble());
            if (buf.isReadable(1)) {
                Components.ROLL.get(player).setFallFlying(buf.readBoolean());
            }
        });
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                var comp = Components.ROLL.get(player);

                if (DoABarrelRoll.handshakeServer.getHandshakeState(player) == HandshakeServer.HandshakeState.ACCEPTED) {
                    comp.setHasClient(true);

                    if (player.isFallFlying()) {
                        Components.ROLL.sync(player);
                    } else if (Components.ROLL.get(player).getRoll() != 0) {
                        comp.setRoll(0);
                        Components.ROLL.sync(player);
                    }
                } else {
                    var hadClient = comp.hasClient();
                    comp.setHasClient(false);

                    if (hadClient) {
                        comp.setRoll(0);
                        Components.ROLL.sync(player);
                    }
                }

                comp.tick();
            });
        });
    }
}
