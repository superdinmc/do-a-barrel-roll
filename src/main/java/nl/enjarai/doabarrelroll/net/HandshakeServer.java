package nl.enjarai.doabarrelroll.net;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.util.DelayedRunnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

public class HandshakeServer<T extends SyncableConfig<T> & ValidatableConfig> {
    private final ServerConfigHolder<T> configHolder;
    private final Map<ServerPlayNetworkHandler, HandshakeState> syncStates = new WeakHashMap<>();
    private final ArrayList<DelayedRunnable> scheduledTasks = new ArrayList<>();
    private final Function<ServerPlayNetworkHandler, Boolean> getsLimitedCheck;
    private final Codec<T> transferCodec;
    private final Codec<? super T> limitedTransferCodec;

    public HandshakeServer(ServerConfigHolder<T> configHolder, Function<ServerPlayNetworkHandler, Boolean> getsLimitedCheck, Codec<T> transferCodec, Codec<? super T> limitedTransferCodec) {
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
        return getHandshakeState(player.networkHandler);
    }

    public HandshakeState getHandshakeState(ServerPlayNetworkHandler handler) {
        return syncStates.getOrDefault(handler, HandshakeState.NOT_SENT);
    }

    public PacketByteBuf getConfigSyncBuf(ServerPlayNetworkHandler handler) {
        var buf = new PacketByteBuf(Unpooled.buffer());

        // Protocol version
        buf.writeInt(2);

        // Config data
        var config = configHolder.instance;
        var isLimited = getsLimitedCheck.apply(handler);
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

    public void configSentToClient(ServerPlayNetworkHandler handler) {
        syncStates.put(handler, HandshakeState.SENT);

        var config = configHolder.instance;
        if (config.getSyncTimeout() != null) {
            scheduledTasks.add(new DelayedRunnable(config.getSyncTimeout(), () -> {
                if (syncStates.getOrDefault(handler, HandshakeState.NOT_SENT) != HandshakeState.ACCEPTED) {
                    DoABarrelRoll.LOGGER.warn(
                            "{} did not accept config syncing, config indicates we kick them.",
                            handler.getPlayer().getName().getString()
                    );
                    handler.disconnect(config.getSyncTimeoutMessage());
                }
            }));
        }
    }

    public HandshakeState clientReplied(ServerPlayNetworkHandler handler, PacketByteBuf buf) {
        var state = getHandshakeState(handler);
        var player = handler.getPlayer();

        if (state == HandshakeState.SENT) {
            try {
                var protocolVersion = buf.readInt();
                if (protocolVersion < 1 || protocolVersion > 2) {
                    syncStates.put(handler, HandshakeState.FAILED);
                    DoABarrelRoll.LOGGER.warn(
                            "Client of {} sent unknown protocol version, expected range 1-2, got {}. Will attempt to proceed anyway.",
                            player.getName().getString(),
                            protocolVersion
                    );
                }

                if (buf.readBoolean()) {
                    syncStates.put(handler, HandshakeState.ACCEPTED);
                    DoABarrelRoll.LOGGER.info("Client of {} accepted server config.", player.getName().getString());
                    return HandshakeState.ACCEPTED;
                } else {
                    syncStates.put(handler, HandshakeState.FAILED);
                    DoABarrelRoll.LOGGER.warn(
                            "Client of {} failed to process server config, check client logs find what went wrong.",
                            player.getName().getString());
                    return HandshakeState.FAILED;
                }
            } catch (IndexOutOfBoundsException e) {
                syncStates.put(handler, HandshakeState.FAILED);
                DoABarrelRoll.LOGGER.warn(
                        "Client of {} sent invalid config reply.",
                        player.getName().getString()
                );
                return HandshakeState.FAILED;
            }
        }

        return state;
    }

    public void playerDisconnected(ServerPlayNetworkHandler handler) {
        syncStates.remove(handler);
    }

    public enum HandshakeState {
        NOT_SENT,
        SENT,
        ACCEPTED,
        FAILED
    }
}
