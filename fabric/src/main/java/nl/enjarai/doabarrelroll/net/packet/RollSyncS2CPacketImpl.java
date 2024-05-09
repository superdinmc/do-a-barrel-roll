package nl.enjarai.doabarrelroll.net.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import nl.enjarai.doabarrelroll.DoABarrelRoll;

public record RollSyncS2CPacketImpl(int entityId, boolean rolling, float roll) implements CustomPayload, RollSyncS2CPacket {
    public static final Id<RollSyncS2CPacketImpl> PACKET_ID = new Id<>(DoABarrelRoll.id("roll_sync"));
    public static final PacketCodec<ByteBuf, RollSyncS2CPacketImpl> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, RollSyncS2CPacketImpl::entityId,
            PacketCodecs.BOOL, RollSyncS2CPacketImpl::rolling,
            PacketCodecs.FLOAT, RollSyncS2CPacketImpl::roll,
            RollSyncS2CPacketImpl::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
