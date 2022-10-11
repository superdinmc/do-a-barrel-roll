package nl.enjarai.doabarrelroll.forge;

import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvents {

    @SubscribeEvent
    public static void onWorldRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            //DoABarrelRollClient.onWorldRender(Minecraft.getInstance(), event.getPartialTick(), event.getPoseStack());
        }
    }

    @SubscribeEvent
    public static void onSetupCamera(ViewportEvent event) {
    }
}
