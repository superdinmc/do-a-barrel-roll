package nl.enjarai.doabarrelroll.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.event.ServerEvents;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import nl.enjarai.doabarrelroll.net.packet.*;

public class ServerNetworking {
    public static final ServerConfigHolder CONFIG_HOLDER = new ServerConfigHolder(
            FabricLoader.getInstance().getConfigDir().resolve(DoABarrelRoll.MODID + "-server.json"),
            ModConfigServer.CODEC, ConfigUpdateAckS2CPacketImpl::new, ServerEvents::updateServerConfig
    );
    public static final HandshakeServer HANDSHAKE_SERVER = new HandshakeServer(
            ConfigSyncS2CPacketImpl::new, CONFIG_HOLDER, player -> !ModConfigServer.canModify(player));

    public static void init() {
        CONFIG_HOLDER.setHandshakeServer(HANDSHAKE_SERVER);

        PayloadTypeRegistry.playC2S().register(ConfigResponseC2SPacketImpl.PACKET_ID, ConfigResponseC2SPacketImpl.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(ConfigUpdateC2SPacketImpl.PACKET_ID, ConfigUpdateC2SPacketImpl.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(RollSyncC2SPacketImpl.PACKET_ID, RollSyncC2SPacketImpl.PACKET_CODEC);

        PayloadTypeRegistry.playS2C().register(ConfigSyncS2CPacketImpl.PACKET_ID, ConfigSyncS2CPacketImpl.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ConfigUpdateAckS2CPacketImpl.PACKET_ID, ConfigUpdateAckS2CPacketImpl.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(RollSyncS2CPacketImpl.PACKET_ID, RollSyncS2CPacketImpl.PACKET_CODEC);
    }
}
