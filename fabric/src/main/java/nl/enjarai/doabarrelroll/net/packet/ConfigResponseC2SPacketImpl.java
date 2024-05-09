package nl.enjarai.doabarrelroll.net.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import nl.enjarai.doabarrelroll.DoABarrelRoll;

public record ConfigResponseC2SPacketImpl(int protocolVersion, boolean success) implements CustomPayload, ConfigResponseC2SPacket {
    public static final Id<ConfigResponseC2SPacketImpl> PACKET_ID = new Id<>(DoABarrelRoll.id("config_response"));
    public static final PacketCodec<ByteBuf, ConfigResponseC2SPacketImpl> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ConfigResponseC2SPacketImpl::protocolVersion,
            PacketCodecs.BOOL, ConfigResponseC2SPacketImpl::success,
            ConfigResponseC2SPacketImpl::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
