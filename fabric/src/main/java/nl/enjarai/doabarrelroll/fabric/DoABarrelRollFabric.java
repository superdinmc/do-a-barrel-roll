package nl.enjarai.doabarrelroll.fabric;

import net.fabricmc.api.ModInitializer;
import nl.enjarai.doabarrelroll.fabric.server.FabricConfigSyncServer;
import nl.enjarai.doabarrelroll.server.ServerModConfig;

public class DoABarrelRollFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerModConfig.load();

        // Register server-side listeners for config syncing, this is done on
        // both client and server to ensure everything works in LAN worlds as well.
        FabricConfigSyncServer.init();
    }
}
