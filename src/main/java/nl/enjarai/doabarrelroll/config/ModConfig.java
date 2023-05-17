package nl.enjarai.doabarrelroll.config;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.config.builder.ConfigBuilder;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;

public class ModConfig {

    public static ModConfig INSTANCE = new ModConfig();

    public static void touch() {
        // touch the grass
    }

    private final ConfigBuilder.Value<Boolean> MOD_ENABLED;

    private final ConfigBuilder.Value<Boolean> SWITCH_ROLL_AND_YAW;
    private final ConfigBuilder.Value<Boolean> MOMENTUM_BASED_MOUSE;
    private final ConfigBuilder.Value<Boolean> SHOW_MOMENTUM_WIDGET;
    private final ConfigBuilder.Value<Boolean> INVERT_PITCH;
    private final ConfigBuilder.Value<ActivationBehaviour> ACTIVATION_BEHAVIOUR;

    private final ConfigBuilder.Value<Boolean> ENABLE_BANKING;
    private final ConfigBuilder.Value<Double> BANKING_STRENGTH;

    private final ConfigBuilder.Value<Boolean> ENABLE_THRUST;
    private final ConfigBuilder.Value<Double> MAX_THRUST;
    private final ConfigBuilder.Value<Double> THRUST_ACCELERATION;
    private final ConfigBuilder.Value<Boolean> THRUST_PARTICLES;

    private final ConfigBuilder.Value<Boolean> SMOOTHING_ENABLED;
    private final ConfigBuilder.Value<Double> SMOOTHING_PITCH;
    private final ConfigBuilder.Value<Double> SMOOTHING_YAW;
    private final ConfigBuilder.Value<Double> SMOOTHING_ROLL;

    private final ConfigBuilder.Value<Double> DESKTOP_SENSITIVITY_PITCH;
    private final ConfigBuilder.Value<Double> DESKTOP_SENSITIVITY_YAW;
    private final ConfigBuilder.Value<Double> DESKTOP_SENSITIVITY_ROLL;

    private final ConfigBuilder.Value<Double> CONTROLLER_SENSITIVITY_PITCH;
    private final ConfigBuilder.Value<Double> CONTROLLER_SENSITIVITY_YAW;
    private final ConfigBuilder.Value<Double> CONTROLLER_SENSITIVITY_ROLL;


    private ModConfig() {
        ConfigBuilder builder = new ConfigBuilder();

        builder.push("general");
        {
            MOD_ENABLED = builder.entry("mod_enabled", true);

            builder.push("controls");
            {
                SWITCH_ROLL_AND_YAW = builder.entry("switch_roll_and_yaw", false).withDescription();
                INVERT_PITCH = builder.entry("invert_pitch", false);
                MOMENTUM_BASED_MOUSE = builder.entry("momentum_based_mouse", false).withDescription();
                SHOW_MOMENTUM_WIDGET = builder.entry("show_momentum_widget", true).withDescription();
                ACTIVATION_BEHAVIOUR = builder.entry("activation_behaviour", ActivationBehaviour.VANILLA).withDescription();
            }
            builder.pop();

            builder.push("banking");
            {
                ENABLE_BANKING = builder.entry("enable_banking", true).withDescription();
                BANKING_STRENGTH = builder.entry("banking_strength", 20.0, 0.0, 100.0);
            }
            builder.pop();

            builder.push("thrust");
            {
                ENABLE_THRUST = builder.entry("enable_thrust", false).withDescription();
                MAX_THRUST = builder.entry("max_thrust", 2.0, 0.1, 10.0).withDescription();
                THRUST_ACCELERATION = builder.entry("thrust_acceleration", 0.01, 0.1, 1.0).withDescription();
                THRUST_PARTICLES = builder.entry("thrust_particles", true);
            }
            builder.pop();
        }
        builder.pop();


        builder.push("sensitivity");
        {
            builder.push("smoothing");
            {
                SMOOTHING_ENABLED = builder.entry("smoothing_enabled", true);
                SMOOTHING_PITCH = builder.entry("smoothing_pitch", 1.0, 0.0, 2.0);
                SMOOTHING_YAW = builder.entry("smoothing_yaw", 0.4, 0.0, 2.0);
                SMOOTHING_ROLL = builder.entry("smoothing_roll", 1.0, 0.0, 2.0);
            }
            builder.pop();

            builder.push("desktop");
            {
                DESKTOP_SENSITIVITY_PITCH = builder.entry("pitch", 1.0, 0.0, 2.0);
                DESKTOP_SENSITIVITY_YAW = builder.entry("yaw", 0.4, 0.0, 2.0);
                DESKTOP_SENSITIVITY_ROLL = builder.entry("roll", 1.0, 0.0, 2.0);
            }
            builder.pop();

            builder.push("controller");
            {
                CONTROLLER_SENSITIVITY_PITCH = builder.entry("pitch", 1.0, 0.0, 2.0).withDescription();
                CONTROLLER_SENSITIVITY_YAW = builder.entry("yaw", 0.6, 0.0, 2.0).withDescription();
                CONTROLLER_SENSITIVITY_ROLL = builder.entry("roll", 1.0, 0.0, 2.0).withDescription();
            }
            builder.pop();
        }
        builder.pop();
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

    public Sensitivity getControllerSensitivity() {
        return new Sensitivity(
                CONTROLLER_SENSITIVITY_PITCH.get(),
                CONTROLLER_SENSITIVITY_YAW.get(),
                CONTROLLER_SENSITIVITY_ROLL.get()
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

    public void setControllerSensitivity(Sensitivity sensitivity) {
        CONTROLLER_SENSITIVITY_PITCH.accept(sensitivity.pitch);
        CONTROLLER_SENSITIVITY_YAW.accept(sensitivity.yaw);
        CONTROLLER_SENSITIVITY_ROLL.accept(sensitivity.roll);
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
                    Text.translatable("toast.do_a_barrel_roll"),
                    Text.translatable("toast.do_a_barrel_roll.thrusting_disabled_by_server")
            ));
        }
        if (serverConfig.forceEnabled() && !MOD_ENABLED.get()) {
            MinecraftClient.getInstance().getToastManager().add(SystemToast.create(
                    MinecraftClient.getInstance(),
                    SystemToast.Type.TUTORIAL_HINT,
                    Text.translatable("toast.do_a_barrel_roll"),
                    Text.translatable("toast.do_a_barrel_roll.mod_forced_enabled_by_server")
            ));
        }
    }
}
