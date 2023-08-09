package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.net.ServerConfigUpdateClient;

public class ServerConfigUpdateClientFabric {
    public static void startListening() {
        ClientPlayNetworking.registerReceiver(DoABarrelRoll.SERVER_CONFIG_UPDATE_CHANNEL, (client, handler, buf, responseSender) -> {
            ServerConfigUpdateClient.updateAcknowledged(buf);
        });
    }
}
