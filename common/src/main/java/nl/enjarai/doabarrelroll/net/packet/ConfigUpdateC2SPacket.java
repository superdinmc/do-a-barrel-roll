package nl.enjarai.doabarrelroll.net.packet;

import nl.enjarai.doabarrelroll.config.ModConfigServer;

public interface ConfigUpdateC2SPacket {
    int protocolVersion();

    ModConfigServer config();
}
