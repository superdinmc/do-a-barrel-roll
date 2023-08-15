package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import nl.enjarai.doabarrelroll.DoABarrelRoll;

public class ServerConfigUpdaterFabric {
    public static void startListening(ServerPlayNetworkHandler handler) {
        ServerPlayNetworking.registerReceiver(handler, DoABarrelRoll.SERVER_CONFIG_UPDATE_CHANNEL, (server, player, handler1, buf, responseSender) -> {
            responseSender.sendPacket(
                    DoABarrelRoll.SERVER_CONFIG_UPDATE_CHANNEL,
                    DoABarrelRoll.CONFIG_HOLDER.clientSendsUpdate(player, buf)
            );
        });
    }
}
