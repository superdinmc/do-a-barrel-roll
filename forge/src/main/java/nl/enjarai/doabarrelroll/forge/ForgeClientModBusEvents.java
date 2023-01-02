package nl.enjarai.doabarrelroll.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nl.enjarai.doabarrelroll.ModKeybindings;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeClientModBusEvents {
    @SubscribeEvent
    public static void onKeyMappingsRegister(RegisterKeyMappingsEvent event) {
        // Register keybindings on forge
        ModKeybindings.ALL.forEach(event::register);
    }
}
