package nl.enjarai.doabarrelroll.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import nl.enjarai.doabarrelroll.config.ModConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModConfig.INSTANCE::generateConfigScreen;
    }
}
