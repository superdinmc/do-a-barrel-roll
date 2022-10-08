package nl.enjarai.doabarrelroll.config;


import dev.architectury.injectables.annotations.ExpectPlatform;

public class ModConfig  {


    @ExpectPlatform
    public static boolean getModEnabled() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean getSwitchRollAndYaw() {
        throw new AssertionError();
    } //= false;

    @ExpectPlatform
    public static boolean getMomentumBasedMouse() {
        throw new AssertionError();
    }// = false;

    @ExpectPlatform
    public static boolean getShowMomentumWidget() {
        throw new AssertionError();
    } //= true;

    @ExpectPlatform
    public static boolean getInvertPitch() {
        throw new AssertionError();
    } //= false;

    @ExpectPlatform
    public static boolean getEnableBanking() {
        throw new AssertionError();
    }// = true;

    @ExpectPlatform
    public static float getBankingStrength() {
        throw new AssertionError();
    }// = 20;

    @ExpectPlatform
    public static boolean getEnableThrust() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float getMaxThrust() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Sensitivity getDesktopSensitivity() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Sensitivity getControllerSensitivity() {
        throw new AssertionError();
    }


    public static RotationInstant configureRotation(RotationInstant rotationInstant) {
        var pitch = rotationInstant.getPitch();
        var yaw = rotationInstant.getYaw();
        var roll = rotationInstant.getRoll();

        if (getSwitchRollAndYaw()) {
            var temp = yaw;
            yaw = roll;
            roll = temp;
        }
        if (getInvertPitch()) {
            pitch = -pitch;
        }

        return new RotationInstant(pitch, yaw, roll, rotationInstant.getRenderDelta());
    }
}
