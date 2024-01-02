package nl.enjarai.doabarrelroll.platform.services;

import net.fabricmc.loader.api.FabricLoader;

public interface PlatformHelper {
    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    static String getEnvironmentName() {
        return FabricLoader.getInstance().isDevelopmentEnvironment() ? "development" : "production";
    }
}