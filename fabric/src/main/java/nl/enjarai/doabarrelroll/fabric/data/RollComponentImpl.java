package nl.enjarai.doabarrelroll.fabric.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.net.HandshakeServer;
import org.jetbrains.annotations.NotNull;

public class RollComponentImpl implements RollComponent {
    private double roll = 0;
    private double lastRoll = 0;
    private boolean hasClient = false;
    private boolean fallFlying = true;

    @Override
    public double getRoll() {
        return roll;
    }

    @Override
    public void setRoll(double roll) {
        this.roll = roll;
    }

    @Override
    public double getLastRoll() {
        return lastRoll;
    }

    @Override
    public void setLastRoll(double lastRoll) {
        this.lastRoll = lastRoll;
    }

    @Override
    public double getRoll(float tickDelta) {
        return MathHelper.lerp(tickDelta, lastRoll, roll);
    }

    @Override
    public boolean hasClient() {
        return hasClient;
    }

    @Override
    public void setHasClient(boolean hasClient) {
        this.hasClient = hasClient;
    }

    @Override
    public boolean isFallFlying() {
        return fallFlying;
    }

    @Override
    public void setFallFlying(boolean fallFlying) {
        this.fallFlying = fallFlying;
    }

    // We don't really need to save this, so both read and write are empty.
    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {}

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {}

    // Sync only with clients that have accepted our handshake
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return DoABarrelRoll.HANDSHAKE_SERVER.getHandshakeState(player) == HandshakeServer.HandshakeState.ACCEPTED;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeDouble(roll);
        buf.writeBoolean(hasClient);
        buf.writeBoolean(fallFlying);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        lastRoll = roll;
        roll = buf.readDouble();
        hasClient = buf.readBoolean();

        if (buf.isReadable(1)) {
            fallFlying = buf.readBoolean();
        }

        if (lastRoll < -90 && roll > 90) {
            lastRoll += 360;
        } else if (lastRoll > 90 && roll < -90) {
            lastRoll -= 360;
        }
    }

    // Update the last roll value every tick, but only on the server
    @Override
    public void tick() {
        lastRoll = roll;
    }
}
