package nl.enjarai.doabarrelroll.fabric;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.bawnorton.mixinsquared.tools.MixinAnnotationReader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.ModKeybindings;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.fabric.net.HandshakeClientFabric;
import nl.enjarai.doabarrelroll.util.StarFoxUtil;

import java.util.List;

public class DoABarrelRollFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DoABarrelRollClient.init();

        ModConfig.touch();
        HandshakeClientFabric.init();

        // Register keybindings on fabric
        ModKeybindings.FABRIC.forEach(KeyBindingHelper::registerKeyBinding);

        // Init barrel rollery.
        StarFoxUtil.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModKeybindings.clientTick(client);

            StarFoxUtil.clientTick(client);
        });
    }
}
