package nl.enjarai.doabarrelroll.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.api.RollEntity;

public class RollSyncClient {
    public static void sendUpdate(RollEntity entity) {
        if (DoABarrelRollClient.HANDSHAKE_CLIENT.hasConnected()) {
            boolean rolling = entity.doABarrelRoll$isRolling();
            float roll = entity.doABarrelRoll$getRoll();

            var buf = PacketByteBufs.create();
            buf.writeBoolean(rolling);
            buf.writeFloat(roll);

            ClientPlayNetworking.send(DoABarrelRoll.ROLL_CHANNEL, buf);
        }
    }

    public static void startListening() {
        ClientPlayNetworking.registerReceiver(DoABarrelRoll.ROLL_CHANNEL, (client, handler1, buf, responseSender) -> {
            if (client.world == null) {
                return;
            }

            int entityId = buf.readInt();
            var isRolling = buf.readBoolean();
            var roll = buf.readFloat();

            var entity = client.world.getEntityById(entityId);
            if (entity == null) {
                return;
            }
            var rollEntity = (RollEntity) entity;

            rollEntity.doABarrelRoll$setRolling(isRolling);
            rollEntity.doABarrelRoll$setRoll(MathHelper.wrapDegrees(roll));
        });
    }
}
