package nl.enjarai.doabarrelroll.net;

import net.minecraft.text.Text;

public interface SyncableConfig<T> {
    Integer getSyncTimeout();

    Text getSyncTimeoutMessage();
}
