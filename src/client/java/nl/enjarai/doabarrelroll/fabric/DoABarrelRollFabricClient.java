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

public class DoABarrelRollFabricClient implements ClientModInitializer, MixinCanceller {
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

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (mixinClassName.equals("com.anthonyhilyard.equipmentcompare.mixin.KeyMappingMixin") && MixinAnnotationReader.getPriority(mixinClassName) == 1000) {
            DoABarrelRoll.LOGGER.warn("Equipment Compare detected, disabling their overly invasive keybinding mixin. Report any relevant issues to them.");
            DoABarrelRoll.LOGGER.warn("If the author of Equipment Compare is reading this: see #31 on your github. Once the issue is fixed, you can set the priority of this mixin to anything other than 1000 to stop it being disabled.");
            return true;
        }
        return false;
    }
}
