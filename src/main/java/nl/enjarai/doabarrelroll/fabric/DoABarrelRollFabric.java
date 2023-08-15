package nl.enjarai.doabarrelroll.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import nl.enjarai.doabarrelroll.fabric.net.HandshakeServerFabric;

public class DoABarrelRollFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Init server and client common code.
        DoABarrelRoll.init();

        // Register server-side listeners for config syncing, this is done on
        // both client and server to ensure everything works in LAN worlds as well.
        HandshakeServerFabric.init();
    }
}
