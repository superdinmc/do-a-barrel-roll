package nl.enjarai.doabarrelroll.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.platform.services.ClientNetworkUtils;

public class FabricClientNetworkUtils implements ClientNetworkUtils {
    @Override
    public void sendPacket(Identifier channel, PacketByteBuf buf) {
        ClientPlayNetworking.send(channel, buf);
    }

    @Override
    public void registerListener(Identifier channel, PacketListener listener) {
        ClientPlayNetworking.registerGlobalReceiver(channel, (client, handler, buf, responseSender) -> {
            listener.accept(buf, replyBuf -> responseSender.sendPacket(channel, replyBuf));
        });
    }
}
