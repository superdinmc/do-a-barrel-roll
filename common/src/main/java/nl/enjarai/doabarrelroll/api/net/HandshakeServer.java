package nl.enjarai.doabarrelroll.api.net;

import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.util.DelayedRunnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class HandshakeServer<T extends SyncableConfig<T>> {
    private final Supplier<T> configSupplier;
    private final Map<ServerPlayerEntity, HandshakeState> syncStates = new WeakHashMap<>();
    private final ArrayList<DelayedRunnable> scheduledTasks = new ArrayList<>();

    public HandshakeServer(Supplier<T> configSupplier) {
        this.configSupplier = configSupplier;
    }

    public void tick(MinecraftServer server) {
        scheduledTasks.removeIf(DelayedRunnable::isDone);
        scheduledTasks.forEach(DelayedRunnable::tick);
    }

    public HandshakeState getHandshakeState(ServerPlayerEntity player) {
        return syncStates.getOrDefault(player, HandshakeState.NOT_SENT);
    }

    public PacketByteBuf getConfigSyncBuf(ServerPlayerEntity player) {
        var buf = new PacketByteBuf(Unpooled.buffer());

        var config = configSupplier.get();
        var data = config.getTransferCodec().encodeStart(JsonOps.INSTANCE, config);
        try {
            buf.writeString(data.getOrThrow(false, System.err::println).toString());
        } catch (RuntimeException e) {
            System.err.println("Failed to encode config\n" + e);
            buf.writeString("{}");
        }

        return buf;
    }

    public void configSentToClient(ServerPlayerEntity player) {
        syncStates.put(player, HandshakeState.SENT);

        var config = configSupplier.get();
        if (config.getSyncTimeout() != null) {
            scheduledTasks.add(new DelayedRunnable(config.getSyncTimeout(), () -> {
                if (syncStates.getOrDefault(player, HandshakeState.NOT_SENT) != HandshakeState.ACCEPTED) {
                    System.out.printf(
                            "%s did not accept config syncing, config indicates we kick them.",
                            player.getName().getString()
                    );
                    player.networkHandler.disconnect(config.getSyncTimeoutMessage());
                }
            }));
        }
    }

    public HandshakeState clientReplied(ServerPlayerEntity player, PacketByteBuf buf) {
        var state = getHandshakeState(player);

        if (state == HandshakeState.SENT) {
            if (buf.readBoolean()) {
                syncStates.put(player, HandshakeState.ACCEPTED);
                System.out.printf("Client of %s accepted server config.", player.getName().getString());
                return HandshakeState.ACCEPTED;
            } else {
                syncStates.put(player, HandshakeState.FAILED);
                System.out.printf(
                        "Client of %s failed to process server config, check client logs find what went wrong.",
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
