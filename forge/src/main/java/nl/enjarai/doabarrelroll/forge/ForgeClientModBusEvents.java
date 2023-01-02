package nl.enjarai.doabarrelroll.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeClientModBusEvents {
    @SubscribeEvent
    public static void onKeyMappingsRegister(RegisterKeyMappingsEvent event) {
        // Register keybindings on forge
        ModKeybindings.ALL.forEach(event::register);
    }
}
