package nl.enjarai.doabarrelroll.platform.services;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.slf4j.Logger;

public interface PlatformHelper {
    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    Logger getLogger();

    boolean checkPermission(ServerPlayNetworkHandler source, String permission, int defaultPermissionLevel);

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    static String getEnvironmentName() {
        return FabricLoader.getInstance().isDevelopmentEnvironment() ? "development" : "production";
    }
}