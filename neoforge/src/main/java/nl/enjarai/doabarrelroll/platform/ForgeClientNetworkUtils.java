package nl.enjarai.doabarrelroll.platform;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.PacketDistributor;
import nl.enjarai.doabarrelroll.DoABarrelRollForge;
import nl.enjarai.doabarrelroll.platform.services.ClientNetworkUtils;

import java.util.ArrayList;

public class ForgeClientNetworkUtils implements ClientNetworkUtils {
    @Override
    public void sendPacket(Identifier channel, PacketByteBuf buf) {
        DoABarrelRollForge.NETWORK_CHANNELS.get(channel).send(PacketDistributor.SERVER.noArg(), buf);
    }

    @Override
    public void registerListener(Identifier channel, PacketListener listener) {
        DoABarrelRollForge.CLIENT_LISTENERS.computeIfAbsent(channel, id -> new ArrayList<>()).add(listener);
    }
}
