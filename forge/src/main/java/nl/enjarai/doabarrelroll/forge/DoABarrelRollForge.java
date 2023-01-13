package nl.enjarai.doabarrelroll.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.config.ModConfig;

@Mod(DoABarrelRoll.MODID)
public class DoABarrelRollForge {
    public DoABarrelRollForge() {
        if (FMLLoader.getDist().isClient()) {
            DoABarrelRollClient.init();

            ModConfig.touch();
        }
    }
}

