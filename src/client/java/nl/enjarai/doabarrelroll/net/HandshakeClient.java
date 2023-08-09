package nl.enjarai.doabarrelroll.net;

import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class HandshakeClient<T> {
    private final Codec<? extends T> transferCodec;
    private final Codec<? extends T> limitedTransferCodec;
    private final Consumer<T> updateCallback;
    private T serverConfig = null;
    private boolean hasConnected = false;

    public HandshakeClient(Codec<? extends T> transferCodec, Codec<? extends T> limitedTransferCodec, Consumer<T> updateCallback) {
        this.transferCodec = transferCodec;
        this.limitedTransferCodec = limitedTransferCodec;
        this.updateCallback = updateCallback;
    }

    /**
     * Returns the server config if the client has received one for this server,
     * returns an empty optional in any other case.
     */
    public Optional<T> getConfig() {
        return Optional.ofNullable(serverConfig);
    }

    public void setConfig(@Nullable T config) {
        serverConfig = config;
        updateCallback.accept(serverConfig);
        hasConnected = serverConfig != null;
    }

    public boolean hasConnected() {
        return hasConnected;
    }

    public PacketByteBuf handleConfigSync(PacketByteBuf buf) {
        try {
            var protocolVersion = buf.readInt();
            if (protocolVersion < 1 || protocolVersion > 2) {
                DoABarrelRoll.LOGGER.warn("Received config with unknown protocol version: {}, will attempt to load anyway", protocolVersion);
            }

            var data = buf.readString();
            var isLimited = true;

            if (protocolVersion >= 2) {
                isLimited = buf.readBoolean();
            }

            var codec = isLimited ? limitedTransferCodec : transferCodec;
            serverConfig = codec.parse(JsonOps.INSTANCE, JsonParser.parseString(data))
                    .getOrThrow(false, DoABarrelRoll.LOGGER::error);
        } catch (RuntimeException e) {
            serverConfig = null;
            DoABarrelRoll.LOGGER.error("Failed to parse config from server", e);
        }

        if (serverConfig != null) {
            updateCallback.accept(serverConfig);
            hasConnected = true;
            DoABarrelRoll.LOGGER.info("Received config from server");
        }

        var returnBuf = new PacketByteBuf(Unpooled.buffer());
        returnBuf.writeInt(2);
        returnBuf.writeBoolean(serverConfig != null);
        return returnBuf;
    }

    public void reset() {
        serverConfig = null;
        hasConnected = false;
    }
}
