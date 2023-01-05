package nl.enjarai.doabarrelroll;

import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.util.ProperLogger;
import org.slf4j.Logger;

public class DoABarrelRoll {
    public static final String MODID = "do_a_barrel_roll";
    public static final Logger LOGGER = ProperLogger.getLogger(MODID);
    public static final Identifier SYNC_CHANNEL = id("config_sync");
    public static final Identifier ROLL_CHANNEL = id("roll");

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
