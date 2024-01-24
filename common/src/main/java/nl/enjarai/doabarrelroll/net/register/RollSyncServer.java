package nl.enjarai.doabarrelroll.net.register;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.net.HandshakeServer;
import nl.enjarai.doabarrelroll.platform.Services;

public class RollSyncServer {
    public static void init() {
        Services.SERVER_NET.registerListener(DoABarrelRoll.ROLL_CHANNEL, (handler, buf, responseSender) -> {
            var rollPlayer = (RollEntity) handler.getPlayer();

            var isRolling = buf.readBoolean();
            var roll = buf.readFloat();

            rollPlayer.doABarrelRoll$setRolling(isRolling);
            rollPlayer.doABarrelRoll$setRoll(isRolling ? MathHelper.wrapDegrees(roll) : 0);
        });
    }

    public static void sendUpdates(Entity entity) {
        var rollEntity = (RollEntity) entity;
        var isRolling = rollEntity.doABarrelRoll$isRolling();
        var roll = rollEntity.doABarrelRoll$getRoll();

        var buf = DoABarrelRoll.createBuf();
        buf.writeInt(entity.getId());
        buf.writeBoolean(isRolling);
        buf.writeFloat(roll);

        Services.SERVER_NET.sendPacketsToTracking(
                entity,
                player -> DoABarrelRoll.HANDSHAKE_SERVER.getHandshakeState(player).state == HandshakeServer.HandshakeState.ACCEPTED,
                DoABarrelRoll.ROLL_CHANNEL, buf
        );
    }
}
