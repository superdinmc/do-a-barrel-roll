package nl.enjarai.doabarrelroll.platform.services;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
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

    void registerNetworkChannels(Identifier... channels);

    void notMyProblem(Screen ConfigScreen, Runnable callback);

    boolean checkModLoaded(String modId);

    boolean isModVersionAtLeast(String modId, String version);
}