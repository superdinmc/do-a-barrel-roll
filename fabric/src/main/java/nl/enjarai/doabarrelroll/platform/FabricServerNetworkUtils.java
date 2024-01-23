package nl.enjarai.doabarrelroll.platform;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.platform.services.ServerNetworkUtils;

import java.util.function.Predicate;

public class FabricServerNetworkUtils implements ServerNetworkUtils {
    @Override
    public void sendPacket(ServerPlayNetworkHandler handler, Identifier channel, PacketByteBuf buf) {
        ServerPlayNetworking.send(handler.getPlayer(), channel, buf);
    }

    @Override
    public void sendPacketsToTracking(Entity entity, Predicate<ServerPlayerEntity> predicate, Identifier channel, PacketByteBuf buf) {
        PlayerLookup.tracking(entity).stream()
                .filter(player -> player != entity)
                .filter(predicate)
                .forEach(player -> ServerPlayNetworking.send(player, DoABarrelRoll.ROLL_CHANNEL, buf));
    }

    @Override
    public void registerListener(Identifier channel, PacketListener listener) {
        ServerPlayNetworking.registerGlobalReceiver(channel, (server, player, handler, buf, responseSender) -> {
            listener.accept(handler, buf, replyBuf -> responseSender.sendPacket(channel, replyBuf));
        });
    }
}
