package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.fabric.data.Components;
import nl.enjarai.doabarrelroll.net.HandshakeServer;

public class RollSyncServer {
    public static void startListening(ServerPlayNetworkHandler handler) {
        ServerPlayNetworking.registerReceiver(handler, DoABarrelRoll.ROLL_CHANNEL, (server, player, handler1, buf, responseSender) -> {
            Components.ROLL.get(player).setRoll(buf.readDouble());
        });
    }

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                if (HandshakeServer.getHandshakeState(player) == HandshakeServer.HandshakeState.ACCEPTED &&
                        player.isFallFlying()) {
                    Components.ROLL.sync(player);
                }
            });
        });
    }
}
