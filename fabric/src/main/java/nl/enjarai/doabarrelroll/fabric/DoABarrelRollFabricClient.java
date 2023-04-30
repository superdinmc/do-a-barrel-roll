package nl.enjarai.doabarrelroll.fabric;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.ModKeybindings;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.fabric.net.HandshakeClientFabric;
import nl.enjarai.doabarrelroll.fabric.net.RollSyncClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DoABarrelRollFabricClient implements ClientModInitializer, PreLaunchEntrypoint {
    public static final Logger LOGGER = LogManager.getLogger(DoABarrelRoll.MODID);

    @Override
    public void onInitializeClient() {
        DoABarrelRollClient.init();

        ModConfig.touch();
        HandshakeClientFabric.init();

        // Register keybindings on fabric
        ModKeybindings.FABRIC.forEach(KeyBindingHelper::registerKeyBinding);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModKeybindings.clientTick(client);

            RollSyncClient.sendUpdate();
        });
    }

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }
}