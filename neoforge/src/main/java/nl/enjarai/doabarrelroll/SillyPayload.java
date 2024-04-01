package nl.enjarai.doabarrelroll;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SillyPayload(PacketByteBuf buf, Identifier id) implements CustomPayload {
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBytes(buf);
    }
}
