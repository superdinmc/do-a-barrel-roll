package nl.enjarai.doabarrelroll.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.config.ModConfig;

@Mod(DoABarrelRollClient.MODID)
public class DoABarrelRollForgeClient {

    public DoABarrelRollForgeClient() {
        ModConfig.touch();
        MinecraftForge.EVENT_BUS.register(ModEvents.class);
    }
}

