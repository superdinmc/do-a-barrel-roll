package nl.enjarai.doabarrelroll.config;


import dev.architectury.injectables.annotations.ExpectPlatform;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigBuilder;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigSpec;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigType;

import java.util.function.Supplier;

public class ModConfig {

    public static ConfigSpec SPEC;
    private static Supplier<Boolean> ENABLED;
    private static Supplier<Boolean> SWITCH_ROLL_AND_YAW;

    public static void init() {
        ConfigBuilder builder = ConfigBuilder.create(DoABarrelRollClient.id("client"), ConfigType.CLIENT);

        ENABLED = builder.comment("Turn On and Off the mod").define("mod_enabled", true);
        SWITCH_ROLL_AND_YAW = builder.comment("Turn On and Off the mod").define("switch_roll_and_yaw", false);

        builder.push("some_category");

        builder.pop();


        SPEC = builder.buildAndRegister();
    }

    public static boolean getModEnabled() {
        return ENABLED.get();
    }

    public static boolean getSwitchRollAndYaw() {
        return SWITCH_ROLL_AND_YAW.get();
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
