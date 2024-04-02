package nl.enjarai.doabarrelroll;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WhyIsTherePublicTransportationInThisModLoaderMod {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlerEvent event) {
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
