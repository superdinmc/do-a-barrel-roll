package nl.enjarai.doabarrelroll.net.register;

import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.platform.Services;

public class ServerConfigUpdaterRegister {
    public static void init() {
        Services.SERVER_NET.registerListener(DoABarrelRoll.SERVER_CONFIG_UPDATE_CHANNEL, (handler, buf, responseSender) -> {
            responseSender.accept(DoABarrelRoll.CONFIG_HOLDER.clientSendsUpdate(handler.getPlayer(), buf));
        });
    }
}
