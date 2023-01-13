package nl.enjarai.doabarrelroll.api.net;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class HandshakeServer<T> {
    private final Supplier<T> configSupplier;
    private final Function<T, String> configSerializer;
    private final Map<ServerPlayerEntity, HandshakeState> syncStates = new WeakHashMap<>();

    public HandshakeServer(Supplier<T> configSupplier, Function<T, String> configSerializer) {
        this.configSupplier = configSupplier;
        this.configSerializer = configSerializer;
    }

    public HandshakeState getHandshakeState(ServerPlayerEntity player) {
        return syncStates.getOrDefault(player, HandshakeState.NOT_SENT);
    }

    public PacketByteBuf getConfigSyncBuf(ServerPlayerEntity player) {
        syncStates.put(player, HandshakeState.SENT);

        var buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(configSerializer.apply(configSupplier.get()));
        return buf;
    }

    public HandshakeState clientReplied(ServerPlayerEntity player, PacketByteBuf buf) {
        var state = getHandshakeState(player);

        if (state == HandshakeState.SENT) {
            if (buf.readBoolean()) {
                syncStates.put(player, HandshakeState.ACCEPTED);
                DoABarrelRoll.LOGGER.info("Client of {} accepted server config.", player.getName().getString());
                return HandshakeState.ACCEPTED;
            } else {
                syncStates.put(player, HandshakeState.FAILED);
                DoABarrelRoll.LOGGER.warn(
                        "Client of {} failed to process server config, check client logs find what went wrong.",
                        player.getName().getString());
                return HandshakeState.FAILED;
            }
        }

        return state;
    }

    public void playerDisconnected(ServerPlayerEntity player) {
        syncStates.remove(player);
    }

    public enum HandshakeState {
        NOT_SENT,
        SENT,
        ACCEPTED,
        FAILED
    }
}
