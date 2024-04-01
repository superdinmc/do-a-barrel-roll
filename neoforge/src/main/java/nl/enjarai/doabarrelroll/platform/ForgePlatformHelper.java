package nl.enjarai.doabarrelroll.platform;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.neoforged.fml.loading.LoadingModList;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.ModPermissions;
import nl.enjarai.doabarrelroll.platform.services.PlatformHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForgePlatformHelper implements PlatformHelper {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public Logger getLogger() {
        return LoggerFactory.getLogger(DoABarrelRoll.MODID);
    }

    @Override
    public boolean checkPermission(ServerPlayNetworkHandler source, String permission, int defaultPermissionLevel) {
        return ModPermissions.resolve(source.getPlayer(), permission, defaultPermissionLevel);
    }

    @Override
    public void registerNetworkChannels(Identifier... channels) {
        // Network channels on NeoForge are registered in an event, so we'll have to do it manually there...
    }

    @Override
    public void notMyProblem(Screen ConfigScreen, Runnable callback) {
        // Get memory leak'd idiots
    }

    @Override
    public boolean checkModLoaded(String modId) {
        return LoadingModList.get().getMods().stream().anyMatch(mod -> mod.getModId().equals(modId));
    }

    @Override
    public boolean isModVersionAtLeast(String modId, String version) {
        // Yea sure it probably is, see if i care!
        return true;
    }
}