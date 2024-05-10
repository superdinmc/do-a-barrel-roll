package nl.enjarai.doabarrelroll.platform;

import net.minecraft.entity.Entity;
import nl.enjarai.doabarrelroll.net.ServerConfigHolder;
import nl.enjarai.doabarrelroll.net.ServerNetworking;
import nl.enjarai.doabarrelroll.platform.services.ServerNetworkUtils;

public class FabricServerNetworkUtils implements ServerNetworkUtils {
    @Override
    public void sendRollUpdates(Entity entity) {
        ServerNetworking.sendRollUpdates(entity);
    }

    @Override
    public ServerConfigHolder<?> getServerConfigHolder() {
        return ServerNetworking.CONFIG_HOLDER;
    }
}
