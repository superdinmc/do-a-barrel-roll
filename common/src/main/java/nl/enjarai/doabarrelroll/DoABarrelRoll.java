package nl.enjarai.doabarrelroll;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.api.event.ServerEvents;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import nl.enjarai.doabarrelroll.net.HandshakeServer;
import nl.enjarai.doabarrelroll.net.register.RollSyncServer;
import nl.enjarai.doabarrelroll.net.ServerConfigHolder;
import nl.enjarai.doabarrelroll.net.register.HandshakeServerRegister;
import nl.enjarai.doabarrelroll.net.register.ServerConfigUpdaterRegister;
import nl.enjarai.doabarrelroll.platform.Services;
import org.slf4j.Logger;

import java.nio.file.Path;

public class DoABarrelRoll {
    public static final String MODID = "do_a_barrel_roll";
    public static final Logger LOGGER = Services.PLATFORM.getLogger();

    public static final Identifier HANDSHAKE_CHANNEL = id("handshake");
    public static final Identifier SERVER_CONFIG_UPDATE_CHANNEL = id("server_config_update");
    public static final Identifier ROLL_CHANNEL = id("player_roll");

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    public static void init() {
        var configFile = Path.of("config");

        Services.PLATFORM.registerNetworkChannels(HANDSHAKE_CHANNEL, SERVER_CONFIG_UPDATE_CHANNEL, ROLL_CHANNEL);

        CONFIG_HOLDER = new ServerConfigHolder<>(configFile,
                ModConfigServer.CODEC, ModConfigServer.DEFAULT, ServerEvents::updateServerConfig);
        HANDSHAKE_SERVER = new HandshakeServer(CONFIG_HOLDER, player -> !ModConfigServer.canModify(player));

        // Register server-side listeners for config syncing, this is done on
        // both client and server to ensure everything works in LAN worlds as well.
        RollSyncServer.init();
        HandshakeServerRegister.init();
        ServerConfigUpdaterRegister.init();
    }

    public static PacketByteBuf createBuf() {
        return new PacketByteBuf(Unpooled.buffer());
    }
}
