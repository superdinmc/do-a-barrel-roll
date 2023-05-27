package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.net.RollSyncClient;

public class HandshakeClientFabric {
    public static void init() {
        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            ClientPlayNetworking.registerReceiver(DoABarrelRoll.HANDSHAKE_CHANNEL, (client1, handler1, buf, responseSender) -> {
                var returnBuf = DoABarrelRollClient.HANDSHAKE_CLIENT.handleConfigSync(buf);
                responseSender.sendPacket(DoABarrelRoll.HANDSHAKE_CHANNEL, returnBuf);

                if (DoABarrelRollClient.HANDSHAKE_CLIENT.hasConnected()) {
                    RollSyncClient.startListening();
                }
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            DoABarrelRollClient.HANDSHAKE_CLIENT.reset();
        });
    }
}
