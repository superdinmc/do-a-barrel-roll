package nl.enjarai.doabarrelroll.platform;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import nl.enjarai.cicada.api.util.ProperLogger;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
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
}
