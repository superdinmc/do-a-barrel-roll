package nl.enjarai.doabarrelroll;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import nl.enjarai.doabarrelroll.net.ServerNetworking;

public class DoABarrelRollFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Init server and client common code.
        DoABarrelRoll.init();

        ServerNetworking.init();
    }
}
