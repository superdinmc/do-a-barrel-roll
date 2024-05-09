package nl.enjarai.doabarrelroll.net.packet;

import nl.enjarai.doabarrelroll.config.LimitedModConfigServer;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import org.jetbrains.annotations.Nullable;

public interface ConfigSyncS2CPacket {
    int protocolVersion();

    LimitedModConfigServer applicableConfig();

    boolean isLimited();

    ModConfigServer fullConfig();
}
