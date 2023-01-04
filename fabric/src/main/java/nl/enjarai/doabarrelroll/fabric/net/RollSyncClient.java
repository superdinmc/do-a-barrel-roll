package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.fabric.data.Components;
import nl.enjarai.doabarrelroll.flight.ElytraMath;

public class RollSyncClient {
    public static void sendUpdate() {
        var client = MinecraftClient.getInstance();

        if (client.player != null) {
            double roll = ElytraMath.getRoll(client.player.getYaw(), DoABarrelRollClient.left);

            if (roll != Components.ROLL.get(client.player).getRoll()) {
                Components.ROLL.get(client.player).setRoll(roll);

                var buf = PacketByteBufs.create();
                buf.writeDouble(roll);

                ClientPlayNetworking.send(DoABarrelRoll.ROLL_CHANNEL, buf);
            }
        }
    }
}
