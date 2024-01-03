package nl.enjarai.doabarrelroll.platform;

import net.minecraft.server.network.ServerPlayNetworkHandler;
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
}