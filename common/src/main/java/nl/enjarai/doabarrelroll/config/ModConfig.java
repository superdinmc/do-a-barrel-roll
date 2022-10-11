package nl.enjarai.doabarrelroll.config;


import dev.architectury.injectables.targets.ArchitecturyTarget;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigBuilder;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigSpec;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigType;

import java.util.Objects;
import java.util.function.Supplier;

public class ModConfig {

    public static ModConfig INSTANCE = new ModConfig();

    public static void touch() {
        // touch the grass
    }

    public ConfigSpec SPEC;
    private final Supplier<Boolean> MOD_ENABLED;


    private final Supplier<Boolean> SWITCH_ROLL_AND_YAW;
    private final Supplier<Boolean> MOMENTUM_BASED_MOUSE;
    private final Supplier<Boolean> SHOW_MOMENTUM_WIDGET;
    private final Supplier<Boolean> INVERT_PITCH;

    private final Supplier<Boolean> ENABLE_BANKING;
    private final Supplier<Double> BANKING_STRENGTH;

    private final Supplier<Boolean> ENABLE_THRUST;
    private final Supplier<Double> MAX_THRUST;

    private final Supplier<Double> DESKTOP_SENSITIVITY_PITCH;
    private final Supplier<Double> DESKTOP_SENSITIVITY_YAW;
    private final Supplier<Double> DESKTOP_SENSITIVITY_ROLL;

    private final Supplier<Double> CONTROLLER_SENSITIVITY_PITCH;
    private final Supplier<Double> CONTROLLER_SENSITIVITY_YAW;
    private final Supplier<Double> CONTROLLER_SENSITIVITY_ROLL;


    private ModConfig() {
        ConfigBuilder builder = ConfigBuilder.create(DoABarrelRollClient.id("client"), ConfigType.CLIENT);

        builder.push("general");
            MOD_ENABLED = builder.define("mod_enabled", true);

            builder.push("controls");
                SWITCH_ROLL_AND_YAW = builder.withDescription().define("switch_roll_and_yaw", false);
                INVERT_PITCH = builder.define("invert_pitch", false);
                MOMENTUM_BASED_MOUSE = builder.withDescription().define("momentum_based_mouse", false);
                SHOW_MOMENTUM_WIDGET = builder.withDescription().define("show_momentum_widget", true);
            builder.pop();

            builder.push("banking");
                ENABLE_BANKING = builder.withDescription().define("enable_banking", true);
                BANKING_STRENGTH = builder.define("banking_strength", 20.0, 0.0, 100.0);
            builder.pop();

            builder.push("thrust");
                ENABLE_THRUST = builder.withDescription().define("enable_thrust", false);
                MAX_THRUST = builder.withDescription().define("max_thrust", 1.0, 0.0, 5.0);
            builder.pop();

        builder.pop();


        builder.push("sensitivity");

            builder.push("desktop");
                DESKTOP_SENSITIVITY_PITCH = builder.define("pitch", 1.0, 0.0, 2.0);
                DESKTOP_SENSITIVITY_YAW = builder.define("yaw", 0.4, 0.0, 2.0);
                DESKTOP_SENSITIVITY_ROLL = builder.define("roll", 1.0, 0.0, 2.0);
            builder.pop();

            if (!Objects.equals(ArchitecturyTarget.getCurrentTarget(), "forge")) {

                builder.push("controller");
                    CONTROLLER_SENSITIVITY_PITCH = builder.withDescription().define("pitch", 1.0, 0.0, 2.0);
                    CONTROLLER_SENSITIVITY_YAW = builder.withDescription().define("yaw", 0.4, 0.0, 2.0);
                    CONTROLLER_SENSITIVITY_ROLL = builder.withDescription().define("roll", 1.0, 0.0, 2.0);
                builder.pop();

            } else {
                CONTROLLER_SENSITIVITY_PITCH = () -> 1.0;
                CONTROLLER_SENSITIVITY_YAW = () -> 0.4;
                CONTROLLER_SENSITIVITY_ROLL = () -> 1.0;
            }

        builder.pop();


        SPEC = builder.buildAndRegister();
    }


    public boolean getModEnabled() {
        return MOD_ENABLED.get();
    }

    public boolean getSwitchRollAndYaw() {
        return SWITCH_ROLL_AND_YAW.get();
    } //= false;

    public boolean getMomentumBasedMouse() {
        return MOMENTUM_BASED_MOUSE.get();
    } //= false;

    public boolean getShowMomentumWidget() {
        return SHOW_MOMENTUM_WIDGET.get();
    } //= true;

    public boolean getInvertPitch() {
        return INVERT_PITCH.get();
    } //= false;

    public boolean getEnableBanking() {
        return ENABLE_BANKING.get();
    }// = true;

    public double getBankingStrength() {
        return BANKING_STRENGTH.get();
    }// = 20;

    public boolean getEnableThrust() {
        return ENABLE_THRUST.get();
    }

    public double getMaxThrust() {
        return MAX_THRUST.get();
    }

    public Sensitivity getDesktopSensitivity() {
        return new Sensitivity(
                DESKTOP_SENSITIVITY_PITCH.get(),
                DESKTOP_SENSITIVITY_YAW.get(),
                DESKTOP_SENSITIVITY_ROLL.get()
        );
    }

    public Sensitivity getControllerSensitivity() {
        return new Sensitivity(
                CONTROLLER_SENSITIVITY_PITCH.get(),
                CONTROLLER_SENSITIVITY_YAW.get(),
                CONTROLLER_SENSITIVITY_ROLL.get()
        );
    }


    public RotationInstant configureRotation(RotationInstant rotationInstant) {
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
