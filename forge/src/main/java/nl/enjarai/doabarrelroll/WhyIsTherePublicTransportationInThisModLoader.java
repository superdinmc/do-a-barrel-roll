package nl.enjarai.doabarrelroll;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;

@Mod.EventBusSubscriber
public class WhyIsTherePublicTransportationInThisModLoader {
    @SubscribeEvent
    public static void gatherPermissions(PermissionGatherEvent.Nodes event) {
        event.addNodes(ModPermissions.NODES.toArray(new PermissionNode[0]));
    }

    @SubscribeEvent
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity serverPlayer) {
            DoABarrelRoll.playerDisconnected(serverPlayer.networkHandler);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            DoABarrelRoll.serverTick(event.getServer());
        }
    }
}
