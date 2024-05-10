package nl.enjarai.doabarrelroll;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import nl.enjarai.doabarrelroll.net.ClientNetworking;

public class DoABarrelRollFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DoABarrelRollClient.init();

        ClientNetworking.init();

        ClientTickEvents.END_CLIENT_TICK.register(EventCallbacksClient::clientTick);

        // Register keybindings on fabric
        ModKeybindings.ALL.forEach(KeyBindingHelper::registerKeyBinding);
    }
}
