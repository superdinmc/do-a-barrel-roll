package nl.enjarai.doabarrelroll.api.net;

import com.google.gson.JsonSyntaxException;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import nl.enjarai.doabarrelroll.DoABarrelRoll;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class HandshakeClient<T> {
    private final Function<String, T> configDeserializer;
    private final Consumer<T> updateCallback;
    private T serverConfig = null;

    public HandshakeClient(Function<String, T> configDeserializer, Consumer<T> updateCallback) {
        this.configDeserializer = configDeserializer;
        this.updateCallback = updateCallback;
    }

    /**
     * Returns the server config if the client has received one for this server,
     * returns an empty optional in any other case.
     */
    public Optional<T> getConfig() {
        return Optional.ofNullable(serverConfig);
    }

    public PacketByteBuf handleConfigSync(PacketByteBuf buf) {
        try {
            serverConfig = configDeserializer.apply(buf.readString());
            DoABarrelRoll.LOGGER.info("Successfully received and applied server config.");
        } catch (JsonSyntaxException e) {
            serverConfig = null;
            DoABarrelRoll.LOGGER.error("Received invalid config from server", e);
        }

        if (serverConfig != null) {
            updateCallback.accept(serverConfig);
        }

        var returnBuf = new PacketByteBuf(Unpooled.buffer());
        returnBuf.writeBoolean(serverConfig != null);
        return returnBuf;
    }

    public void reset() {
        serverConfig = null;
    }
}
