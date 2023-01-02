package nl.enjarai.doabarrelroll.server;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;

import java.util.Map;
import java.util.WeakHashMap;

public class ConfigSyncServer {
    public static final Map<ServerPlayerEntity, ConfigSyncState> SYNC_STATES = new WeakHashMap<>();

    public static ConfigSyncState getSyncState(ServerPlayerEntity player) {
        return SYNC_STATES.getOrDefault(player, ConfigSyncState.NOT_SENT);
    }

    public static PacketByteBuf getConfigSyncBuf(ServerPlayerEntity player) {
        SYNC_STATES.put(player, ConfigSyncState.SENT);

        var buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(ServerModConfig.INSTANCE.toJson());
        return buf;
    }

    public static void clientReplied(ServerPlayerEntity player, PacketByteBuf buf) {
        if (getSyncState(player) == ConfigSyncState.SENT) {
            if (buf.readBoolean()) {
                SYNC_STATES.put(player, ConfigSyncState.ACCEPTED);
                DoABarrelRoll.LOGGER.info("Client of {} accepted server config.", player.getName().getString());
            } else {
                SYNC_STATES.put(player, ConfigSyncState.FAILED);
                DoABarrelRoll.LOGGER.warn(
                        "Client of {} failed to process server config, check client logs find what went wrong.",
                        player.getName().getString());
            }
        }
    }

    public static void playerDisconnected(ServerPlayerEntity player) {
        SYNC_STATES.remove(player);
    }

    public enum ConfigSyncState {
        NOT_SENT,
        SENT,
        ACCEPTED,
        FAILED
    }
}
