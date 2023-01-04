package nl.enjarai.doabarrelroll.fabric.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import nl.enjarai.doabarrelroll.net.HandshakeServer;
import org.jetbrains.annotations.NotNull;

public class RollComponentImpl implements RollComponent {
    private double roll = 0;

    @Override
    public double getRoll() {
        return roll;
    }

    @Override
    public void setRoll(double roll) {
        this.roll = roll;
    }

    // We don't really need to save this, so both read and write are empty.
    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {}

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {}

    // Sync only with clients that have accepted our handshake
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return HandshakeServer.getHandshakeState(player) == HandshakeServer.HandshakeState.ACCEPTED;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeDouble(roll);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        roll = buf.readDouble();
    }
}
