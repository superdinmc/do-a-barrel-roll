package nl.enjarai.doabarrelroll.fabric.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.data.Components;
import nl.enjarai.doabarrelroll.flight.ElytraMath;

public class RollSyncClient {
    public static void sendUpdate() {
        var client = MinecraftClient.getInstance();

        if (client.player != null) {
            double roll = ElytraMath.getRoll(client.player.getYaw(), DoABarrelRollClient.left);
            boolean rolling = ((RollEntity) client.player).doABarrelRoll$isRolling();

            if (roll != Components.ROLL.get(client.player).getRoll() ||
                    rolling != Components.ROLL.get(client.player).isFallFlying()) {
                Components.ROLL.get(client.player).setRoll(roll);
                Components.ROLL.get(client.player).setFallFlying(rolling);

                var buf = PacketByteBufs.create();
                buf.writeDouble(roll);
                buf.writeBoolean(rolling);

                ClientPlayNetworking.send(DoABarrelRoll.ROLL_CHANNEL, buf);
            }
        }
    }
}
