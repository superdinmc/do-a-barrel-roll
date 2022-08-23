package nl.enjarai.doabarrelroll.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;

@Config(name = DoABarrelRollClient.MODID)
public class ModConfig implements ConfigData {
    public static ModConfig INSTANCE;

    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    @ConfigEntry.Gui.Tooltip
    public boolean switchRollAndYaw = false;
}
