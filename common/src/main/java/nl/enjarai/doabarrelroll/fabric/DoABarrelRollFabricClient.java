package nl.enjarai.doabarrelroll.fabric;

import net.fabricmc.api.ClientModInitializer;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;

public class DoABarrelRollFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DoABarrelRollClient.init();
    }
}
