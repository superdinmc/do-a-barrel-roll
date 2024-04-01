package nl.enjarai.doabarrelroll;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WhyIsTherePublicTransportationInThisModLoaderClientMod {
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        ModKeybindings.ALL.forEach(event::register);
    }
}
