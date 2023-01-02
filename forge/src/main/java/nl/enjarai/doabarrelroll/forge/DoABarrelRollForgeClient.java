package nl.enjarai.doabarrelroll.forge;

import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.ModKeybindings;
import nl.enjarai.doabarrelroll.config.ModConfig;

@Mod(DoABarrelRoll.MODID)
public class DoABarrelRollForgeClient {

    public DoABarrelRollForgeClient() {
        ModConfig.touch();

        ModKeybindings.ALL.forEach(ClientRegistry::registerKeyBinding);
    }
}

