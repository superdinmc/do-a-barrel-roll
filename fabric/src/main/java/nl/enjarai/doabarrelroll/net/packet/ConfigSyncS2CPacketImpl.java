package nl.enjarai.doabarrelroll.net.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.config.LimitedModConfigServer;
import nl.enjarai.doabarrelroll.config.ModConfigServer;
import org.jetbrains.annotations.Nullable;

public record ConfigSyncS2CPacketImpl(int protocolVersion, LimitedModConfigServer applicableConfig, boolean isLimited, ModConfigServer fullConfig) implements CustomPayload, ConfigSyncS2CPacket {
    public static final Id<ConfigSyncS2CPacketImpl> PACKET_ID = new Id<>(DoABarrelRoll.id("config_sync"));
    public static final PacketCodec<PacketByteBuf, ConfigSyncS2CPacketImpl> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ConfigSyncS2CPacketImpl::protocolVersion,
            LimitedModConfigServer.getPacketCodec(), ConfigSyncS2CPacketImpl::applicableConfig,
            PacketCodecs.BOOL, ConfigSyncS2CPacketImpl::isLimited,
            ModConfigServer.PACKET_CODEC, ConfigSyncS2CPacketImpl::fullConfig,
            ConfigSyncS2CPacketImpl::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
