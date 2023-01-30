package nl.enjarai.doabarrelroll.forge;

import net.minecraft.client.MinecraftClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nl.enjarai.doabarrelroll.ModKeybindings;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ForgeClientBusEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ModKeybindings.clientTick(MinecraftClient.getInstance());
        }
    }
}
