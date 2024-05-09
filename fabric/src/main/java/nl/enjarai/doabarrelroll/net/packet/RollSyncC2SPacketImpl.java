package nl.enjarai.doabarrelroll.net.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import nl.enjarai.doabarrelroll.DoABarrelRoll;

public record RollSyncC2SPacketImpl(boolean rolling, float roll) implements CustomPayload, RollSyncC2SPacket {
    public static final Id<RollSyncC2SPacketImpl> PACKET_ID = new Id<>(DoABarrelRoll.id("roll_sync"));
    public static final PacketCodec<ByteBuf, RollSyncC2SPacketImpl> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, RollSyncC2SPacketImpl::rolling,
            PacketCodecs.FLOAT, RollSyncC2SPacketImpl::roll,
            RollSyncC2SPacketImpl::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
