package nl.enjarai.doabarrelroll.net;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.config.ServerModConfig;

import java.util.Map;
import java.util.WeakHashMap;

public class HandshakeServer {
    public static final Map<ServerPlayerEntity, HandshakeState> SYNC_STATES = new WeakHashMap<>();

    public static HandshakeState getHandshakeState(ServerPlayerEntity player) {
        return SYNC_STATES.getOrDefault(player, HandshakeState.NOT_SENT);
    }

    public static PacketByteBuf getConfigSyncBuf(ServerPlayerEntity player) {
        SYNC_STATES.put(player, HandshakeState.SENT);

        var buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(ServerModConfig.INSTANCE.toJson());
        return buf;
    }

    public static HandshakeState clientReplied(ServerPlayerEntity player, PacketByteBuf buf) {
        var state = getHandshakeState(player);

        if (state == HandshakeState.SENT) {
            if (buf.readBoolean()) {
                SYNC_STATES.put(player, HandshakeState.ACCEPTED);
                DoABarrelRoll.LOGGER.info("Client of {} accepted server config.", player.getName().getString());
                return HandshakeState.ACCEPTED;
            } else {
                SYNC_STATES.put(player, HandshakeState.FAILED);
                DoABarrelRoll.LOGGER.warn(
                        "Client of {} failed to process server config, check client logs find what went wrong.",
                        player.getName().getString());
                return HandshakeState.FAILED;
            }
        }

        return state;
    }

    public static void playerDisconnected(ServerPlayerEntity player) {
        SYNC_STATES.remove(player);
    }

    public enum HandshakeState {
        NOT_SENT,
        SENT,
        ACCEPTED,
        FAILED
    }
}
