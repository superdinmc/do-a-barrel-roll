package nl.enjarai.doabarrelroll.platform;

import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import nl.enjarai.doabarrelroll.net.ClientNetworking;
import nl.enjarai.doabarrelroll.net.HandshakeClient;
import nl.enjarai.doabarrelroll.platform.services.ClientNetworkUtils;

public class FabricClientNetworkUtils implements ClientNetworkUtils {
    @Override
    public void sendRollUpdate(RollEntity entity) {
        ClientNetworking.sendRollUpdate(entity);
    }

    @Override
    public void sendConfigUpdatePacket(ModConfigServer config) {
        ClientNetworking.sendConfigUpdatePacket(config);
    }

    @Override
    public HandshakeClient<?> getHandshakeClient() {
        return ClientNetworking.HANDSHAKE_CLIENT;
    }
}
