package nl.enjarai.doabarrelroll.platform.services;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public interface ClientNetworkUtils {
    void sendPacket(Identifier channel, PacketByteBuf buf);

    void registerListener(Identifier channel, PacketListener listener);

    interface PacketListener {
        void accept(PacketByteBuf buf, Consumer<PacketByteBuf> responseSender);
    }
}
