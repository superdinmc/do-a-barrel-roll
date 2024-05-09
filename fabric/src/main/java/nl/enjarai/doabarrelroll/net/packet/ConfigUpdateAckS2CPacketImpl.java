package nl.enjarai.doabarrelroll.net.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import nl.enjarai.doabarrelroll.DoABarrelRoll;

public record ConfigUpdateAckS2CPacketImpl(int protocolVersion, boolean success) implements CustomPayload, ConfigUpdateAckS2CPacket {
    public static final Id<ConfigUpdateAckS2CPacketImpl> PACKET_ID = new Id<>(DoABarrelRoll.id("config_update_ack"));
    public static final PacketCodec<ByteBuf, ConfigUpdateAckS2CPacketImpl> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ConfigUpdateAckS2CPacketImpl::protocolVersion,
            PacketCodecs.BOOL, ConfigUpdateAckS2CPacketImpl::success,
            ConfigUpdateAckS2CPacketImpl::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
