package nl.enjarai.doabarrelroll.net.register;

import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.platform.Services;

public class HandshakeClientRegister {
    public static void init() {
        Services.CLIENT_NET.registerListener(DoABarrelRoll.HANDSHAKE_CHANNEL, (buf, responseSender) -> {
            var returnBuf = DoABarrelRollClient.HANDSHAKE_CLIENT.handleConfigSync(buf);
            responseSender.accept(returnBuf);

            if (DoABarrelRollClient.HANDSHAKE_CLIENT.hasConnected()) {
                RollSyncClient.init();
                ServerConfigUpdateClientRegister.startListening();
            }
        });
    }
}
