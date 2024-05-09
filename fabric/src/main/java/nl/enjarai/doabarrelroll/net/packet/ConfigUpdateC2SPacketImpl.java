package nl.enjarai.doabarrelroll.net.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.config.ModConfigServer;

public record ConfigUpdateC2SPacketImpl(int protocolVersion, ModConfigServer config) implements CustomPayload, ConfigUpdateC2SPacket {
    public static final Id<ConfigUpdateC2SPacketImpl> PACKET_ID = new Id<>(DoABarrelRoll.id("config_update"));
    public static final PacketCodec<ByteBuf, ConfigUpdateC2SPacketImpl> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ConfigUpdateC2SPacketImpl::protocolVersion,
            ModConfigServer.PACKET_CODEC, ConfigUpdateC2SPacketImpl::config,
            ConfigUpdateC2SPacketImpl::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
