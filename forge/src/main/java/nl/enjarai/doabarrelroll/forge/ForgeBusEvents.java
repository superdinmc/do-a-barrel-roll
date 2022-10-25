package nl.enjarai.doabarrelroll.forge;

import net.minecraft.client.MinecraftClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ForgeBusEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            DoABarrelRollClient.clientTick(MinecraftClient.getInstance());
        }
    }
}
