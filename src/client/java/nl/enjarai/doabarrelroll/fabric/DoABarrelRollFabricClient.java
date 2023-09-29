package nl.enjarai.doabarrelroll.fabric;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.ProperLogger;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.ModKeybindings;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.fabric.net.HandshakeClientFabric;
import nl.enjarai.doabarrelroll.util.MixinCancelChecker;
import nl.enjarai.doabarrelroll.util.StarFoxUtil;
import org.slf4j.Logger;

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
        if (mixinClassName.equals("com.anthonyhilyard.equipmentcompare.mixin.KeyMappingMixin") && !MixinCancelChecker.hasChangedPriority(mixinClassName, 0)) {
            DoABarrelRoll.LOGGER.warn("Equipment Compare detected, disabling their overly invasive keybinding mixin. Report any relevant issues to them.");
            DoABarrelRoll.LOGGER.warn("If the author of Equipment Compare is reading this: see #31 on your github. Once you've fixed the issue, you can set the priority of this mixin explicitly to stop it being disabled.");
            return true;
        }
        return false;
    }
}
