package nl.enjarai.doabarrelroll;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class EventCallbacks {
    public static void serverTick(MinecraftServer server) {
        DoABarrelRoll.HANDSHAKE_SERVER.tick(server);
    }

    public static void playerDisconnected(ServerPlayNetworkHandler handler) {
        DoABarrelRoll.HANDSHAKE_SERVER.playerDisconnected(handler);
    }
}
