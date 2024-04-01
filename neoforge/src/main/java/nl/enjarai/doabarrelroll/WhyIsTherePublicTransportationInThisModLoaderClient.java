package nl.enjarai.doabarrelroll;

import net.minecraft.client.MinecraftClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.TickEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class WhyIsTherePublicTransportationInThisModLoaderClient {
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EventCallbacksClient.clientTick(MinecraftClient.getInstance());
        }
    }

    @SubscribeEvent
    public static void loggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        EventCallbacksClient.clientDisconnect();
    }
}
