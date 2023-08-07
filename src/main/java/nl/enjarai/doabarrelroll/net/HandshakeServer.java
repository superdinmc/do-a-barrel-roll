package nl.enjarai.doabarrelroll.net;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.util.DelayedRunnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

public class HandshakeServer<T extends SyncableConfig<T> & ValidatableConfig> {
    private final ServerConfigHolder<T> configHolder;
    private final Map<ServerPlayerEntity, HandshakeState> syncStates = new WeakHashMap<>();
    private final ArrayList<DelayedRunnable> scheduledTasks = new ArrayList<>();
    private final Function<ServerPlayerEntity, Boolean> getsLimitedCheck;
    private final Codec<T> transferCodec;
    private final Codec<? super T> limitedTransferCodec;

    public HandshakeServer(ServerConfigHolder<T> configHolder, Function<ServerPlayerEntity, Boolean> getsLimitedCheck, Codec<T> transferCodec, Codec<? super T> limitedTransferCodec) {
        this.configHolder = configHolder;
        this.getsLimitedCheck = getsLimitedCheck;
        this.transferCodec = transferCodec;
        this.limitedTransferCodec = limitedTransferCodec;
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

        // Protocol version
        buf.writeInt(2);

        // Config data
        var config = configHolder.instance;
        var isLimited = getsLimitedCheck.apply(player);
        Codec<? super T> codec = isLimited ? limitedTransferCodec : transferCodec;
        var data = codec.encodeStart(JsonOps.INSTANCE, config);
        try {
            buf.writeString(data.getOrThrow(false, DoABarrelRoll.LOGGER::error).toString());
        } catch (RuntimeException e) {
            DoABarrelRoll.LOGGER.error("Failed to encode config", e);
            buf.writeString("{}");
        }

        // Limited status
        buf.writeBoolean(isLimited);

        return buf;
    }

    public void configSentToClient(ServerPlayerEntity player) {
        syncStates.put(player, HandshakeState.SENT);

        var config = configHolder.instance;
        if (config.getSyncTimeout() != null) {
            scheduledTasks.add(new DelayedRunnable(config.getSyncTimeout(), () -> {
                if (syncStates.getOrDefault(player, HandshakeState.NOT_SENT) != HandshakeState.ACCEPTED) {
                    DoABarrelRoll.LOGGER.warn(
                            "{} did not accept config syncing, config indicates we kick them.",
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
            try {
                var protocolVersion = buf.readInt();
                if (protocolVersion < 1 || protocolVersion > 2) {
                    syncStates.put(player, HandshakeState.FAILED);
                    DoABarrelRoll.LOGGER.warn(
                            "Client of {} sent unknown protocol version, expected range 1-2, got {}. Will attempt to proceed anyway.",
                            player.getName().getString(),
                            protocolVersion
                    );
                }

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
            } catch (IndexOutOfBoundsException e) {
                syncStates.put(player, HandshakeState.FAILED);
                DoABarrelRoll.LOGGER.warn(
                        "Client of {} sent invalid config reply.",
                        player.getName().getString()
                );
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
