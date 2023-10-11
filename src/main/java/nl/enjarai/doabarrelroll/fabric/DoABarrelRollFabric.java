package nl.enjarai.doabarrelroll.fabric;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.bawnorton.mixinsquared.tools.MixinAnnotationReader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import nl.enjarai.doabarrelroll.fabric.net.HandshakeServerFabric;

import java.util.List;

public class DoABarrelRollFabric implements ModInitializer, MixinCanceller {
    @Override
    public void onInitialize() {
        // Init server and client common code.
        DoABarrelRoll.init();

        // Register server-side listeners for config syncing, this is done on
        // both client and server to ensure everything works in LAN worlds as well.
        HandshakeServerFabric.init();
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
