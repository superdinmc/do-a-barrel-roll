package nl.enjarai.doabarrelroll.net;

import net.minecraft.network.PacketByteBuf;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import nl.enjarai.doabarrelroll.net.packet.ConfigUpdateAckS2CPacket;
import nl.enjarai.doabarrelroll.net.packet.ConfigUpdateC2SPacket;
import nl.enjarai.doabarrelroll.util.ToastUtil;

public class ServerConfigUpdateClient {
    private final PacketConstructor packetConstructor;
    private boolean waitingForAck = false;

    public ServerConfigUpdateClient(PacketConstructor packetConstructor) {
        this.packetConstructor = packetConstructor;
    }

    public ConfigUpdateC2SPacket prepUpdatePacket(ModConfigServer config) {
        waitingForAck = true;
        return packetConstructor.construct(HandshakeServer.PROTOCOL_VERSION, config);
    }

    public void updateAcknowledged(ConfigUpdateAckS2CPacket packet) {
        if (waitingForAck) {
            waitingForAck = false;

            var protocolVersion = packet.protocolVersion();
            if (protocolVersion != 1) {
                DoABarrelRoll.LOGGER.warn("Received config update ack with unknown protocol version: {}, will attempt to read anyway", protocolVersion);
            }

            var success = packet.success();
            if (success) {
                // I don't think we need this
//                    var data = ModConfigServer.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(buf.readString()))
//                            .getOrThrow(false, DoABarrelRoll.LOGGER::warn).getFirst();
//                    DoABarrelRollClient.HANDSHAKE_CLIENT.setConfig(data);

                ToastUtil.toasty("server_config_updated");
            } else {
                ToastUtil.toasty("server_config_update_failed");
            }
        }
    }

    public interface PacketConstructor {
        ConfigUpdateC2SPacket construct(int protocolVersion, ModConfigServer config);
    }
}
