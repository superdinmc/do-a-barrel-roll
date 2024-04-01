package nl.enjarai.doabarrelroll;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;

import java.util.Objects;

@Mod.EventBusSubscriber
public class WhyIsTherePublicTransportationInThisModLoader {
    @SubscribeEvent
    public static void gatherPermissions(PermissionGatherEvent.Nodes event) {
        event.addNodes(ModPermissions.NODES.toArray(new PermissionNode[0]));
    }

    @SubscribeEvent
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity serverPlayer) {
            EventCallbacks.playerDisconnected(serverPlayer.networkHandler);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EventCallbacks.serverTick(event.getServer());
        }
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(DoABarrelRoll.MODID);
        registerChannel(registrar, DoABarrelRoll.HANDSHAKE_CHANNEL);
        registerChannel(registrar, DoABarrelRoll.ROLL_CHANNEL);
    }

    private static void registerChannel(IPayloadRegistrar registrar, Identifier id) {
        registrar.play(id, buf -> new SillyPayload(buf, id), (payload, ctx) -> {
            if (ctx.flow().getReceptionSide().isServer()) {
                DoABarrelRollForge.SERVER_LISTENERS.get(id).forEach(
                        listener -> listener.accept(((ServerPlayerEntity) ctx.player().orElseThrow()).networkHandler, payload.buf(),
                                replyBuf -> ctx.replyHandler().send(new SillyPayload(replyBuf, id))));
            } else {
                DoABarrelRollForge.CLIENT_LISTENERS.get(id).forEach(
                        listener -> listener.accept(payload.buf(), replyBuf -> ctx.replyHandler().send(new SillyPayload(replyBuf, id))));
            }
        });
    }
}
