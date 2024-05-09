package nl.enjarai.doabarrelroll.net;

import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.net.ServerConfigUpdateClient;
import nl.enjarai.doabarrelroll.platform.Services;

public class ServerConfigUpdateClientRegister {
    public static void startListening() {
        Services.CLIENT_NET.registerListener(DoABarrelRoll.SERVER_CONFIG_UPDATE_CHANNEL, (buf, responseSender) -> {
            ServerConfigUpdateClient.updateAcknowledged(buf);
        });
    }
}
