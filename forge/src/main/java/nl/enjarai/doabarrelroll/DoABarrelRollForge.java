package nl.enjarai.doabarrelroll;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import nl.enjarai.doabarrelroll.config.ModConfigScreen;

@Mod(DoABarrelRoll.MODID)
public class DoABarrelRollForge {
    public DoABarrelRollForge() {
        DoABarrelRoll.init();
        if (FMLLoader.getDist().isClient()) {
            DoABarrelRollClient.init();
        }

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> ModConfigScreen.create(parent)
                )
        );
    }
}
