package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.net.HandshakeClient;

public class HandshakeClientFabric {
    public static void init() {
        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            ClientPlayNetworking.registerReceiver(DoABarrelRoll.SYNC_CHANNEL, (client1, handler1, buf, responseSender) -> {
                var returnBuf = HandshakeClient.handleConfigSync(buf);
                responseSender.sendPacket(DoABarrelRoll.SYNC_CHANNEL, returnBuf);
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            HandshakeClient.reset();
        });
    }
}
