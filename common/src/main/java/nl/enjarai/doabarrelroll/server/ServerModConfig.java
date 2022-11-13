package nl.enjarai.doabarrelroll.server;

import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigBuilder;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigSpec;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigType;
import nl.enjarai.doabarrelroll.util.Value;

public class ServerModConfig {
    public static ServerModConfig INSTANCE = new ServerModConfig();

    public static void touch() {
        // touch the grass
    }

    public ConfigSpec SPEC;

    private final Value<Boolean> ALLOW_THRUSTING;

    private ServerModConfig() {
        ConfigBuilder builder = ConfigBuilder.create(DoABarrelRollClient.id("server"), ConfigType.COMMON);

        builder.push("general");
            ALLOW_THRUSTING = builder.define("allow_thrusting", true);
        builder.pop();

        SPEC = builder.build();
    }

    public boolean getAllowThrusting() {
        return ALLOW_THRUSTING.get();
    }

    public void setAllowThrusting(boolean allowThrusting) {
        ALLOW_THRUSTING.accept(allowThrusting);
    }

    public void save() {
        SPEC.save();
    }
}
