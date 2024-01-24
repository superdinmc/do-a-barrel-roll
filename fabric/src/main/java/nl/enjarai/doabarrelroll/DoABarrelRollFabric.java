package nl.enjarai.doabarrelroll;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class DoABarrelRollFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Init server and client common code.
        DoABarrelRoll.init();

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            EventCallbacks.playerDisconnected(handler);
        });
        ServerTickEvents.END_SERVER_TICK.register(EventCallbacks::serverTick);
    }
}
