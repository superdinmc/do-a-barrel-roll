package nl.enjarai.doabarrelroll.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import nl.enjarai.doabarrelroll.fabric.net.HandshakeServerFabric;
import nl.enjarai.doabarrelroll.config.ServerModConfig;
import nl.enjarai.doabarrelroll.fabric.net.RollSyncServer;

public class DoABarrelRollFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register server-side listeners for config syncing, this is done on
        // both client and server to ensure everything works in LAN worlds as well.
        ServerModConfig.load();
        HandshakeServerFabric.init();

        // Register server-side listeners for roll syncing.
        RollSyncServer.init();
    }
}
