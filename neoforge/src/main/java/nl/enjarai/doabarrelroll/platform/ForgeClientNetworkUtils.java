package nl.enjarai.doabarrelroll.platform;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.DoABarrelRollForge;
import nl.enjarai.doabarrelroll.SillyPayload;
import nl.enjarai.doabarrelroll.platform.services.ClientNetworkUtils;

import java.util.ArrayList;

public class ForgeClientNetworkUtils implements ClientNetworkUtils {
    @Override
    public void sendPacket(Identifier channel, PacketByteBuf buf) {
        var handler = MinecraftClient.getInstance().getNetworkHandler();
        if (handler != null) {
            handler.send(new SillyPayload(buf, channel));
        }
    }

    @Override
    public void registerListener(Identifier channel, PacketListener listener) {
        DoABarrelRollForge.CLIENT_LISTENERS.computeIfAbsent(channel, id -> new ArrayList<>()).add(listener);
    }
}
