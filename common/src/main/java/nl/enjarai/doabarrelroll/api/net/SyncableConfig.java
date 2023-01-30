package nl.enjarai.doabarrelroll.api.net;

import com.mojang.serialization.Codec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public interface SyncableConfig<SELF extends SyncableConfig<SELF>> {
    Integer getSyncTimeout();

    Text getSyncTimeoutMessage();

    Codec<? super SELF> getTransferCodec();
}
