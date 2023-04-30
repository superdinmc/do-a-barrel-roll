package nl.enjarai.doabarrelroll.config;


import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.TranslatableText;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigBuilder;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigSpec;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigType;
import nl.enjarai.doabarrelroll.util.Value;

import java.util.Objects;

public class ModConfig {

    public static ModConfig INSTANCE = new ModConfig();

    public static void touch() {
        // touch the grass
    }

    public ConfigSpec SPEC;

    private final Value<Boolean> MOD_ENABLED;

    private final Value<Boolean> SWITCH_ROLL_AND_YAW;
    private final Value<Boolean> MOMENTUM_BASED_MOUSE;
    private final Value<Boolean> SHOW_MOMENTUM_WIDGET;
    private final Value<Boolean> INVERT_PITCH;
    private final Value<ActivationBehaviour> ACTIVATION_BEHAVIOUR;

    private final Value<Boolean> ENABLE_BANKING;
    private final Value<Double> BANKING_STRENGTH;

    private final Value<Boolean> ENABLE_THRUST;
    private final Value<Double> MAX_THRUST;
    private final Value<Double> THRUST_ACCELERATION;
    private final Value<Boolean> THRUST_PARTICLES;

    private final Value<Boolean> SMOOTHING_ENABLED;
    private final Value<Double> SMOOTHING_PITCH;
    private final Value<Double> SMOOTHING_YAW;
    private final Value<Double> SMOOTHING_ROLL;

    private final Value<Double> DESKTOP_SENSITIVITY_PITCH;
    private final Value<Double> DESKTOP_SENSITIVITY_YAW;
    private final Value<Double> DESKTOP_SENSITIVITY_ROLL;


    private ModConfig() {
        ConfigBuilder builder = ConfigBuilder.create(DoABarrelRoll.id("client"), ConfigType.CLIENT);

        builder.push("general");
        {
            MOD_ENABLED = builder.define("mod_enabled", true);

            builder.push("controls");
            {
                SWITCH_ROLL_AND_YAW = builder.withDescription().define("switch_roll_and_yaw", false);
                INVERT_PITCH = builder.define("invert_pitch", false);
                MOMENTUM_BASED_MOUSE = builder.withDescription().define("momentum_based_mouse", false);
                SHOW_MOMENTUM_WIDGET = builder.withDescription().define("show_momentum_widget", true);
                ACTIVATION_BEHAVIOUR = builder.withDescription().define("activation_behaviour", ActivationBehaviour.VANILLA);
            }
            builder.pop();

            builder.push("banking");
            {
                ENABLE_BANKING = builder.withDescription().define("enable_banking", true);
                BANKING_STRENGTH = builder.define("banking_strength", 20.0, 0.0, 100.0);
            }
            builder.pop();

            if (!Objects.equals(ArchitecturyTarget.getCurrentTarget(), "forge")) {
                builder.push("thrust");
                {
                    ENABLE_THRUST = builder.withDescription().define("enable_thrust", false);
                    MAX_THRUST = builder.withDescription().define("max_thrust", 2.0, 0.1, 10.0);
                    THRUST_ACCELERATION = builder.withDescription().define("thrust_acceleration", 0.01, 0.1, 1.0);
                    THRUST_PARTICLES = builder.define("thrust_particles", true);
                }
                builder.pop();

            } else {
                ENABLE_THRUST = Value.of(false);
                MAX_THRUST = Value.of(2.0);
                THRUST_ACCELERATION = Value.of(0.1);
                THRUST_PARTICLES = Value.of(true);
            }

        }
        builder.pop();


        builder.push("sensitivity");
        {
            builder.push("smoothing");
            {
                SMOOTHING_ENABLED = builder.define("smoothing_enabled", true);
                SMOOTHING_PITCH = builder.define("smoothing_pitch", 1.0, 0.0, 2.0);
                SMOOTHING_YAW = builder.define("smoothing_yaw", 0.4, 0.0, 2.0);
                SMOOTHING_ROLL = builder.define("smoothing_roll", 1.0, 0.0, 2.0);
            }
            builder.pop();

            builder.push("desktop");
            {
                DESKTOP_SENSITIVITY_PITCH = builder.define("pitch", 1.0, 0.0, 2.0);
                DESKTOP_SENSITIVITY_YAW = builder.define("yaw", 0.4, 0.0, 2.0);
                DESKTOP_SENSITIVITY_ROLL = builder.define("roll", 1.0, 0.0, 2.0);
            }
            builder.pop();
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

    public ActivationBehaviour getActivationBehaviour() {
        return ACTIVATION_BEHAVIOUR.get();
    } //= ActivationBehaviour.VANILLA;

    public boolean getEnableBanking() {
        return ENABLE_BANKING.get();
    }// = true;

    public double getBankingStrength() {
        return BANKING_STRENGTH.get();
    }// = 20;

    public boolean getEnableThrust() {
        return ENABLE_THRUST.get() && DoABarrelRollClient.HANDSHAKE_CLIENT
                .getConfig().map(SyncedModConfig::allowThrusting).orElse(true);
    }

    public double getMaxThrust() {
        return MAX_THRUST.get();
    }

    public double getThrustAcceleration() {
        return THRUST_ACCELERATION.get();
    }

    public boolean getThrustParticles() {
        return THRUST_PARTICLES.get();
    }

    public boolean getSmoothingEnabled() {
        return SMOOTHING_ENABLED.get();
    }

    public Sensitivity getSmoothing() {
        return new Sensitivity(
                SMOOTHING_PITCH.get(),
                SMOOTHING_YAW.get(),
                SMOOTHING_ROLL.get()
        );
    }

    public Sensitivity getDesktopSensitivity() {
        return new Sensitivity(
                DESKTOP_SENSITIVITY_PITCH.get(),
                DESKTOP_SENSITIVITY_YAW.get(),
                DESKTOP_SENSITIVITY_ROLL.get()
        );
    }

    public void setModEnabled(boolean enabled) {
        MOD_ENABLED.accept(enabled);
    }

    public void setSwitchRollAndYaw(boolean enabled) {
        SWITCH_ROLL_AND_YAW.accept(enabled);
    }

    public void setMomentumBasedMouse(boolean enabled) {
        MOMENTUM_BASED_MOUSE.accept(enabled);
    }

    public void setShowMomentumWidget(boolean enabled) {
        SHOW_MOMENTUM_WIDGET.accept(enabled);
    }

    public void setInvertPitch(boolean enabled) {
        INVERT_PITCH.accept(enabled);
    }

    public void setActivationBehaviour(ActivationBehaviour behaviour) {
        ACTIVATION_BEHAVIOUR.accept(behaviour);
    }

    public void setEnableBanking(boolean enabled) {
        ENABLE_BANKING.accept(enabled);
    }

    public void setBankingStrength(double strength) {
        BANKING_STRENGTH.accept(strength);
    }

    public void setEnableThrust(boolean enabled) {
        ENABLE_THRUST.accept(enabled);
    }

    public void setMaxThrust(double thrust) {
        MAX_THRUST.accept(thrust);
    }

    public void setThrustAcceleration(double acceleration) {
        THRUST_ACCELERATION.accept(acceleration);
    }

    public void setThrustParticles(boolean enabled) {
        THRUST_PARTICLES.accept(enabled);
    }

    public void setSmoothingEnabled(boolean enabled) {
        SMOOTHING_ENABLED.accept(enabled);
    }

    public void setDesktopSensitivity(Sensitivity sensitivity) {
        DESKTOP_SENSITIVITY_PITCH.accept(sensitivity.pitch);
        DESKTOP_SENSITIVITY_YAW.accept(sensitivity.yaw);
        DESKTOP_SENSITIVITY_ROLL.accept(sensitivity.roll);
    }

    public void save() {
        SPEC.save();
    }

    public RotationInstant configureRotation(RotationInstant rotationInstant) {
        var pitch = rotationInstant.getPitch();
        var yaw = rotationInstant.getYaw();
        var roll = rotationInstant.getRoll();

        if (!getSwitchRollAndYaw()) {
            var temp = yaw;
            yaw = roll;
            roll = temp;
        }
        if (getInvertPitch()) {
            pitch = -pitch;
        }

        return new RotationInstant(pitch, yaw, roll, rotationInstant.getRenderDelta());
    }

    public void notifyPlayerOfServerConfig(SyncedModConfig serverConfig) {
        if (!serverConfig.allowThrusting() && ENABLE_THRUST.get()) {
            MinecraftClient.getInstance().getToastManager().add(SystemToast.create(
                    MinecraftClient.getInstance(),
                    SystemToast.Type.TUTORIAL_HINT,
                    new TranslatableText("toast.do_a_barrel_roll"),
                    new TranslatableText("toast.do_a_barrel_roll.thrusting_disabled_by_server")
            ));
        }
        if (serverConfig.forceEnabled() && !MOD_ENABLED.get()) {
            MinecraftClient.getInstance().getToastManager().add(SystemToast.create(
                    MinecraftClient.getInstance(),
                    SystemToast.Type.TUTORIAL_HINT,
                    new TranslatableText("toast.do_a_barrel_roll"),
                    new TranslatableText("toast.do_a_barrel_roll.mod_forced_enabled_by_server")
            ));
        }
    }
}