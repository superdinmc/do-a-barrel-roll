package nl.enjarai.doabarrelroll.fabric;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.ModKeybindings;
import nl.enjarai.doabarrelroll.config.ModConfig;

public class DoABarrelRollFabricClient implements ClientModInitializer, PreLaunchEntrypoint {

    @Override
    public void onInitializeClient() {
        ModConfig.touch();

        // Register keybindings on fabric
        ModKeybindings.ALL.forEach(KeyBindingRegistryImpl::registerKeyBinding);

        ClientTickEvents.END_CLIENT_TICK.register(DoABarrelRollClient::clientTick);
    }

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }
}
