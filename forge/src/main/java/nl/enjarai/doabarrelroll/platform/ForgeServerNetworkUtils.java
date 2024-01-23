package nl.enjarai.doabarrelroll.platform;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.PacketDistributor;
import nl.enjarai.doabarrelroll.DoABarrelRollForge;
import nl.enjarai.doabarrelroll.platform.services.ServerNetworkUtils;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ForgeServerNetworkUtils implements ServerNetworkUtils {
    @Override
    public void sendPacket(ServerPlayNetworkHandler handler, Identifier channel, PacketByteBuf buf) {
        DoABarrelRollForge.NETWORK_CHANNELS.get(channel).send(PacketDistributor.PLAYER.with(handler::getPlayer), buf);
    }

    @Override
    public void sendPacketsToTracking(Entity entity, Predicate<ServerPlayerEntity> predicate, Identifier channel, PacketByteBuf buf) {
        // Cant do predicates here apparently? Well fuck me.
        DoABarrelRollForge.NETWORK_CHANNELS.get(channel).send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), buf);
    }

    @Override
    public void registerListener(Identifier channel, PacketListener listener) {
        DoABarrelRollForge.SERVER_LISTENERS.computeIfAbsent(channel, id -> new ArrayList<>()).add(listener);
    }
}
