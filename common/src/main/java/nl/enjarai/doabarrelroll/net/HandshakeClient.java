package nl.enjarai.doabarrelroll.net;

import com.google.gson.JsonSyntaxException;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.config.ServerModConfig;

import java.util.Optional;

public class HandshakeClient {
    private static ServerModConfig serverConfig = null;

    /**
     * Returns the server config if the client has received one for this server,
     * returns an empty optional in any other case.
     */
    public static Optional<ServerModConfig> getConfig() {
        return Optional.ofNullable(serverConfig);
    }

    public static PacketByteBuf handleConfigSync(PacketByteBuf buf) {
        try {
            serverConfig = ServerModConfig.fromJson(buf.readString());
            DoABarrelRoll.LOGGER.info("Successfully received and applied server config.");
        } catch (JsonSyntaxException e) {
            serverConfig = null;
            DoABarrelRoll.LOGGER.error("Received invalid config from server", e);
        }

        if (serverConfig != null) {
            ModConfig.INSTANCE.notifyPlayerOfServerConfig(serverConfig);
        }

        var returnBuf = new PacketByteBuf(Unpooled.buffer());
        returnBuf.writeBoolean(serverConfig != null);
        return returnBuf;
    }

    public static void reset() {
        serverConfig = null;
    }
}
