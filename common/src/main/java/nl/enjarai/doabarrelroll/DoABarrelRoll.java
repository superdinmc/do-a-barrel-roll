package nl.enjarai.doabarrelroll;

import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.api.net.HandshakeServer;
import nl.enjarai.doabarrelroll.config.SyncedModConfigServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DoABarrelRoll {
    public static final String MODID = "do_a_barrel_roll";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final Identifier SYNC_CHANNEL = id("config_sync");
    public static final HandshakeServer<SyncedModConfigServer> HANDSHAKE_SERVER =
            new HandshakeServer<>(() -> SyncedModConfigServer.INSTANCE);
    public static final Identifier ROLL_CHANNEL = id("roll");

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
