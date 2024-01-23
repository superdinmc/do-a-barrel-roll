package nl.enjarai.doabarrelroll.platform.services;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ServerNetworkUtils {
    void sendPacket(ServerPlayNetworkHandler handler, Identifier channel, PacketByteBuf buf);

    void sendPacketsToTracking(Entity entity, Predicate<ServerPlayerEntity> predicate, Identifier channel, PacketByteBuf buf);

    void registerListener(Identifier channel, PacketListener listener);

    interface PacketListener {
        void accept(ServerPlayNetworkHandler source, PacketByteBuf buf, Consumer<PacketByteBuf> responseSender);
    }
}
