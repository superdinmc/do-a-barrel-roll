package nl.enjarai.doabarrelroll.platform;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.api.util.ProperLogger;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.event.ClientEvents;
import nl.enjarai.doabarrelroll.platform.services.PlatformHelper;
import org.slf4j.Logger;

public class FabricPlatformHelper implements PlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public Logger getLogger() {
        return ProperLogger.getLogger(DoABarrelRoll.MODID);
    }

    @Override
    public boolean checkPermission(ServerPlayNetworkHandler source, String permission, int defaultPermissionLevel) {
        return Permissions.check(source.getPlayer(), permission, defaultPermissionLevel);
    }

    @Override
    public void registerNetworkChannels(Identifier... channels) {
        // No-op
    }

    @Override
    public void notMyProblem(Screen configScreen, Runnable callback) {
        ScreenEvents.remove(configScreen).register(screen1 -> callback.run());
    }

    @Override
    public boolean checkModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isModVersionAtLeast(String modId, String version) {
        try {
            var parsed = Version.parse(version);
            return FabricLoader.getInstance().getModContainer("yet_another_config_lib_v3")
                    .filter(modContainer -> modContainer.getMetadata().getVersion().compareTo(parsed) >= 0)
                    .isPresent();
        } catch (VersionParsingException e) {
            throw new RuntimeException("Skill issue, bad version");
        }
    }
}
