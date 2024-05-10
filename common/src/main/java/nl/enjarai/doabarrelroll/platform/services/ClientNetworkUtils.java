package nl.enjarai.doabarrelroll.platform.services;

import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import nl.enjarai.doabarrelroll.net.HandshakeClient;

public interface ClientNetworkUtils {
    void sendRollUpdate(RollEntity entity);

    void sendConfigUpdatePacket(ModConfigServer config);

    HandshakeClient<?> getHandshakeClient();
}
