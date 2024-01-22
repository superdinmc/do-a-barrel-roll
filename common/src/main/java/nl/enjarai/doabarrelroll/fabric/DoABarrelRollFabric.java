package nl.enjarai.doabarrelroll.fabric;

import net.fabricmc.api.ModInitializer;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.fabric.net.HandshakeServerFabric;

public class DoABarrelRollFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Init server and client common code.
        DoABarrelRoll.init();
    }
}
