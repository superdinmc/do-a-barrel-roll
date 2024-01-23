package nl.enjarai.doabarrelroll.compat;

import nl.enjarai.doabarrelroll.platform.Services;

public class Compat {
    public static final String YACL_MIN_VERSION = "3.1.0";

    public static boolean isYACLLoaded() {
         return Services.PLATFORM.checkModLoaded("yet_another_config_lib_v3");
    }

    public static boolean isYACLUpToDate() {
        return Services.PLATFORM.isModVersionAtLeast("yet_another_config_lib_v3", YACL_MIN_VERSION);
    }
}
