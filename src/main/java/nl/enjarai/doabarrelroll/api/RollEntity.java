package nl.enjarai.doabarrelroll.api;

import nl.enjarai.doabarrelroll.config.Sensitivity;

public interface RollEntity {
    void doABarrelRoll$changeElytraLook(double pitch, double yaw, double roll, Sensitivity sensitivity, double delta);

    void doABarrelRoll$changeElytraLook(float pitch, float yaw, float roll);

    boolean doABarrelRoll$isRolling();

    float doABarrelRoll$getRoll();

    float doABarrelRoll$getRoll(float tickDelta);
}
