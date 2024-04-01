package nl.enjarai.doabarrelroll.platform;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.DoABarrelRollForge;
import nl.enjarai.doabarrelroll.SillyPayload;
import nl.enjarai.doabarrelroll.platform.services.ServerNetworkUtils;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ForgeServerNetworkUtils implements ServerNetworkUtils {
    @Override
    public void sendPacket(ServerPlayNetworkHandler handler, Identifier channel, PacketByteBuf buf) {
        handler.send(new SillyPayload(buf, channel));
    }

    @Override
    public void sendPacketsToTracking(Entity entity, Predicate<ServerPlayerEntity> predicate, Identifier channel, PacketByteBuf buf) {
        entity.getWorld().getPlayers().stream()
                .filter(player -> player != entity)
                // Temporary solution until I can figure out how to find tracked players on NeoForge.
                .filter(player -> entity.getEyePos().squaredDistanceTo(player.getPos()) <= 16 * 10)
                .filter(player -> predicate.test((ServerPlayerEntity) player))
                .forEach(player -> ((ServerPlayerEntity) player).networkHandler.send(new SillyPayload(buf, channel)));
    }

    @Override
    public void registerListener(Identifier channel, PacketListener listener) {
        DoABarrelRollForge.SERVER_LISTENERS.computeIfAbsent(channel, id -> new ArrayList<>()).add(listener);
    }
}
