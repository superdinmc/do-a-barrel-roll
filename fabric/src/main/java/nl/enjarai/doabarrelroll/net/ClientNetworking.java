package nl.enjarai.doabarrelroll.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.api.event.ClientEvents;
import nl.enjarai.doabarrelroll.net.packet.ConfigResponseC2SPacketImpl;
import nl.enjarai.doabarrelroll.net.packet.ConfigSyncS2CPacketImpl;

public class ClientNetworking {
    public static final HandshakeClient HANDSHAKE_CLIENT = new HandshakeClient(
            ConfigResponseC2SPacketImpl::new,
            ClientEvents::updateServerConfig
    );

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncS2CPacketImpl.PACKET_ID, (payload, context) -> {
            var response = HANDSHAKE_CLIENT.handleConfigSync(payload);
            context.responseSender().sendPacket(response);

            if (HANDSHAKE_CLIENT.hasConnected()) {
                RollSyncClient.init();
                ServerConfigUpdateClientRegister.startListening();
            }
        });
    }
}
