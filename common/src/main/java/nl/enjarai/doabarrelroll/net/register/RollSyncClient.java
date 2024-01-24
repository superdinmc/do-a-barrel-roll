package nl.enjarai.doabarrelroll.net.register;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.platform.Services;

public class RollSyncClient {
    public static void init() {
        Services.CLIENT_NET.registerListener(DoABarrelRoll.ROLL_CHANNEL, (buf, responseSender) -> {
            var client = MinecraftClient.getInstance();
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

    public static void sendUpdate(RollEntity entity) {
        if (DoABarrelRollClient.HANDSHAKE_CLIENT.hasConnected()) {
            boolean rolling = entity.doABarrelRoll$isRolling();
            float roll = entity.doABarrelRoll$getRoll();

            var buf = DoABarrelRoll.createBuf();
            buf.writeBoolean(rolling);
            buf.writeFloat(roll);

            Services.CLIENT_NET.sendPacket(DoABarrelRoll.ROLL_CHANNEL, buf);
        }
    }
}
