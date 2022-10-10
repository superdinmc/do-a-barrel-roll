package nl.enjarai.doabarrelroll.fabric;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import nl.enjarai.doabarrelroll.config.ModConfig;

public class DoABarrelRollFabricClient implements ClientModInitializer, PreLaunchEntrypoint {

    @Override
    public void onInitializeClient() {
        ModConfig.init();
    }

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }
}
