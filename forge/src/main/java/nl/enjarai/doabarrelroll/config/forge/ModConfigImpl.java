package nl.enjarai.doabarrelroll.config.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import nl.enjarai.doabarrelroll.forge.ModEvents;

public class ModConfigImpl {

    private static ForgeConfigSpec.BooleanValue MOD_ENABLED;

    public static boolean getModEnabled() {
        return MOD_ENABLED.get();
    }

    public static void init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        MOD_ENABLED = builder.comment("mod enabled").define("mod_enabled",true);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());

    }
}
