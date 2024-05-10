package nl.enjarai.doabarrelroll.platform.services;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.net.ServerConfigHolder;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ServerNetworkUtils {
    void sendRollUpdates(Entity entity);

    ServerConfigHolder<?> getServerConfigHolder();
}
