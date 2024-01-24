package nl.enjarai.doabarrelroll;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class DoABarrelRollFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DoABarrelRollClient.init();

        ClientTickEvents.END_CLIENT_TICK.register(EventCallbacksClient::clientTick);
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> EventCallbacksClient.clientDisconnect());

        // Register keybindings on fabric
        ModKeybindings.ALL.forEach(KeyBindingHelper::registerKeyBinding);
    }
}
