package nl.enjarai.doabarrelroll.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.slider.DoubleSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ModConfig {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    public static final Path CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("do_a_barrel_roll-client.json");
    public static ModConfig INSTANCE = loadConfigFile(CONFIG_FILE.toFile());

    public static void touch() {
        // touch the grass
    }

    General general = new General();
    static class General {
        boolean mod_enabled = true;

        Controls controls = new Controls();
        static class Controls {
            boolean switch_roll_and_yaw = false;
            boolean invert_pitch = false;
            boolean momentum_based_mouse = false;
            boolean show_momentum_widget = true;
            ActivationBehaviour activation_behaviour = ActivationBehaviour.VANILLA;
        }

        Hud hud = new Hud();
        static class Hud {
            boolean show_horizon = false;
        }

        Banking banking = new Banking();
        static class Banking {
            boolean enable_banking = true;
            double banking_strength = 20.0;
        }

        Thrust thrust = new Thrust();
        static class Thrust {
            boolean enable_thrust = false;
            double max_thrust = 2.0;
            double thrust_acceleration = 0.1;
            boolean thrust_particles = true;
        }
    }

    SensitivityConfig sensitivity = new SensitivityConfig();
    static class SensitivityConfig {
        Smoothing smoothing = new Smoothing();
        static class Smoothing {
            boolean smoothing_enabled = true;
            double smoothing_pitch = 1.0;
            double smoothing_yaw = 0.4;
            double smoothing_roll = 1.0;
        }

        Sensitivity desktop = new Sensitivity();
        Sensitivity controller = new Sensitivity();
    }


    public Screen generateConfigScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(getText("title"))
                .category(ConfigCategory.createBuilder()
                        .name(getText("general"))
                        .option(getBooleanOption("general", "mod_enabled", false)
                                .binding(true, () -> general.mod_enabled, value -> general.mod_enabled = value)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(getText("controls"))
                                .option(getBooleanOption("controls", "switch_roll_and_yaw", true)
                                        .binding(false, () -> general.controls.switch_roll_and_yaw, value -> general.controls.switch_roll_and_yaw = value)
                                        .build())
                                .option(getBooleanOption("controls", "invert_pitch", false)
                                        .binding(false, () -> general.controls.invert_pitch, value -> general.controls.invert_pitch = value)
                                        .build())
                                .option(getBooleanOption("controls", "momentum_based_mouse", true)
                                        .binding(false, () -> general.controls.momentum_based_mouse, value -> general.controls.momentum_based_mouse = value)
                                        .build())
                                .option(getOption(ActivationBehaviour.class, "controls", "activation_behaviour", true)
                                        .controller(EnumController::new)
                                        .binding(ActivationBehaviour.VANILLA, () -> general.controls.activation_behaviour, value -> general.controls.activation_behaviour = value)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(getText("hud"))
                                .option(getBooleanOption("hud", "show_horizon", true)
                                        .binding(false, () -> general.hud.show_horizon, value -> general.hud.show_horizon = value)
                                        .build())
                                .option(getBooleanOption("controls", "show_momentum_widget", true)
                                        .binding(true, () -> general.controls.show_momentum_widget, value -> general.controls.show_momentum_widget = value)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(getText("banking"))
                                .option(getBooleanOption("banking", "enable_banking", true)
                                        .binding(true, () -> general.banking.enable_banking, value -> general.banking.enable_banking = value)
                                        .build())
                                .option(getOption(Double.class, "banking", "banking_strength", false)
                                        .controller(option -> new DoubleSliderController(option, 0.0, 100.0, 1.0))
                                        .binding(20.0, () -> general.banking.banking_strength, value -> general.banking.banking_strength = value)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(getText("thrust"))
                                .collapsed(true)
                                .option(getBooleanOption("thrust", "enable_thrust", true)
                                        .binding(false, () -> general.thrust.enable_thrust, value -> general.thrust.enable_thrust = value)
                                        .build())
                                .option(getOption(Double.class, "thrust", "max_thrust", true)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 10.0, 0.1))
                                        .binding(2.0, () -> general.thrust.max_thrust, value -> general.thrust.max_thrust = value)
                                        .build())
                                .option(getOption(Double.class, "thrust", "thrust_acceleration", true)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 1.0, 0.1))
                                        .binding(0.1, () -> general.thrust.thrust_acceleration, value -> general.thrust.thrust_acceleration = value)
                                        .build())
                                .option(getBooleanOption("thrust", "thrust_particles", false)
                                        .binding(true, () -> general.thrust.thrust_particles, value -> general.thrust.thrust_particles = value)
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(getText("sensitivity"))
                        .group(OptionGroup.createBuilder()
                                .name(getText("smoothing"))
                                .option(getBooleanOption("smoothing", "smoothing_enabled", false)
                                        .binding(true, () -> sensitivity.smoothing.smoothing_enabled, value -> sensitivity.smoothing.smoothing_enabled = value)
                                        .build())
                                .option(getOption(Double.class, "smoothing", "smoothing_pitch", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 5.0, 0.1))
                                        .binding(1.0, () -> sensitivity.smoothing.smoothing_pitch, value -> sensitivity.smoothing.smoothing_pitch = value)
                                        .build())
                                .option(getOption(Double.class, "smoothing", "smoothing_yaw", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 5.0, 0.1))
                                        .binding(0.4, () -> sensitivity.smoothing.smoothing_yaw, value -> sensitivity.smoothing.smoothing_yaw = value)
                                        .build())
                                .option(getOption(Double.class, "smoothing", "smoothing_roll", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 5.0, 0.1))
                                        .binding(1.0, () -> sensitivity.smoothing.smoothing_roll, value -> sensitivity.smoothing.smoothing_roll = value)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(getText("desktop"))
                                .option(getOption(Double.class, "desktop", "pitch", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 10.0, 0.1))
                                        .binding(1.0, () -> sensitivity.desktop.pitch, value -> sensitivity.desktop.pitch = value)
                                        .build())
                                .option(getOption(Double.class, "desktop", "yaw", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 10.0, 0.1))
                                        .binding(0.4, () -> sensitivity.desktop.yaw, value -> sensitivity.desktop.yaw = value)
                                        .build())
                                .option(getOption(Double.class, "desktop", "roll", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 10.0, 0.1))
                                        .binding(1.0, () -> sensitivity.desktop.roll, value -> sensitivity.desktop.roll = value)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(getText("controller"))
                                .collapsed(!(FabricLoader.getInstance().isModLoaded("controlify") || FabricLoader.getInstance().isModLoaded("midnightcontrols")))
                                .tooltip(getText("controller.description"))
                                .option(getOption(Double.class, "controller", "pitch", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 10.0, 0.1))
                                        .binding(1.0, () -> sensitivity.controller.pitch, value -> sensitivity.controller.pitch = value)
                                        .build())
                                .option(getOption(Double.class, "controller", "yaw", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 10.0, 0.1))
                                        .binding(0.4, () -> sensitivity.controller.yaw, value -> sensitivity.controller.yaw = value)
                                        .build())
                                .option(getOption(Double.class, "controller", "roll", false)
                                        .controller(option -> new DoubleSliderController(option, 0.1, 10.0, 0.1))
                                        .binding(1.0, () -> sensitivity.controller.roll, value -> sensitivity.controller.roll = value)
                                        .build())
                                .build())
                        .build())
                .save(this::save)
                .build()
                .generateScreen(parent);
    }

    private <T> Option.Builder<T> getOption(Class<T> clazz, String category, String key, boolean description) {
        Option.Builder<T> builder = Option.createBuilder(clazz)
                .name(getText(category, key));
        if (description) {
            builder.tooltip(getText(category, key + ".description"));
        }
        return builder;
    }

    private Option.Builder<Boolean> getBooleanOption(String category, String key, boolean description) {
        return getOption(Boolean.class, category, key, description)
                .controller(TickBoxController::new);
    }

    private Text getText(String category, String key) {
        return Text.translatable("config.do_a_barrel_roll." + category + "." + key);
    }

    private Text getText(String key) {
        return Text.translatable("config.do_a_barrel_roll." + key);
    }


    public boolean getModEnabled() {
        return general.mod_enabled;
    }

    public boolean getSwitchRollAndYaw() {
        return general.controls.switch_roll_and_yaw;
    } //= false;

    public boolean getMomentumBasedMouse() {
        return general.controls.momentum_based_mouse;
    } //= false;

    public boolean getShowMomentumWidget() {
        return general.controls.show_momentum_widget;
    } //= true;

    public boolean getInvertPitch() {
        return general.controls.invert_pitch;
    } //= false;

    public ActivationBehaviour getActivationBehaviour() {
        return general.controls.activation_behaviour;
    } //= ActivationBehaviour.VANILLA;

    public boolean getShowHorizon() {
        return general.hud.show_horizon;
    }

    public boolean getEnableBanking() {
        return general.banking.enable_banking;
    }// = true;

    public double getBankingStrength() {
        return general.banking.banking_strength;
    }// = 20;

    public boolean getEnableThrust() {
        return general.thrust.enable_thrust && DoABarrelRollClient.HANDSHAKE_CLIENT
                .getConfig().map(SyncedModConfig::allowThrusting).orElse(true);
    }

    public double getMaxThrust() {
        return general.thrust.max_thrust;
    }

    public double getThrustAcceleration() {
        return general.thrust.thrust_acceleration;
    }

    public boolean getThrustParticles() {
        return general.thrust.thrust_particles;
    }

    public boolean getSmoothingEnabled() {
        return sensitivity.smoothing.smoothing_enabled;
    }

    public Sensitivity getSmoothing() {
        return new Sensitivity(
                sensitivity.smoothing.smoothing_pitch,
                sensitivity.smoothing.smoothing_yaw,
                sensitivity.smoothing.smoothing_roll
        );
    }

    public Sensitivity getDesktopSensitivity() {
        return sensitivity.desktop;
    }

    public Sensitivity getControllerSensitivity() {
        return sensitivity.controller;
    }

    public void setModEnabled(boolean enabled) {
        general.mod_enabled = enabled;
    }

    public void setSwitchRollAndYaw(boolean enabled) {
        general.controls.switch_roll_and_yaw = enabled;
    }

    public void setMomentumBasedMouse(boolean enabled) {
        general.controls.momentum_based_mouse = enabled;
    }

    public void setShowMomentumWidget(boolean enabled) {
        general.controls.show_momentum_widget = enabled;
    }

    public void setInvertPitch(boolean enabled) {
        general.controls.invert_pitch = enabled;
    }

    public void setActivationBehaviour(ActivationBehaviour behaviour) {
        general.controls.activation_behaviour = behaviour;
    }

    public void setShowHorizon(boolean enabled) {
        general.hud.show_horizon = enabled;
    }

    public void setEnableBanking(boolean enabled) {
        general.banking.enable_banking = enabled;
    }

    public void setBankingStrength(double strength) {
        general.banking.banking_strength = strength;
    }

    public void setEnableThrust(boolean enabled) {
        general.thrust.enable_thrust = enabled;
    }

    public void setMaxThrust(double thrust) {
        general.thrust.max_thrust = thrust;
    }

    public void setThrustAcceleration(double acceleration) {
        general.thrust.thrust_acceleration = acceleration;
    }

    public void setThrustParticles(boolean enabled) {
        general.thrust.thrust_particles = enabled;
    }

    public void setSmoothingEnabled(boolean enabled) {
        sensitivity.smoothing.smoothing_enabled = enabled;
    }

    public void setDesktopSensitivity(Sensitivity sensitivity) {
        this.sensitivity.desktop = sensitivity;
    }

    public void setControllerSensitivity(Sensitivity sensitivity) {
        this.sensitivity.controller = sensitivity;
    }

    public void save() {
        saveConfigFile(CONFIG_FILE.toFile());
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
        if (!serverConfig.allowThrusting() && general.thrust.enable_thrust) {
            MinecraftClient.getInstance().getToastManager().add(SystemToast.create(
                    MinecraftClient.getInstance(),
                    SystemToast.Type.TUTORIAL_HINT,
                    Text.translatable("toast.do_a_barrel_roll"),
                    Text.translatable("toast.do_a_barrel_roll.thrusting_disabled_by_server")
            ));
        }
        if (serverConfig.forceEnabled() && !general.mod_enabled) {
            MinecraftClient.getInstance().getToastManager().add(SystemToast.create(
                    MinecraftClient.getInstance(),
                    SystemToast.Type.TUTORIAL_HINT,
                    Text.translatable("toast.do_a_barrel_roll"),
                    Text.translatable("toast.do_a_barrel_roll.mod_forced_enabled_by_server")
            ));
        }
    }

    /**
     * Loads config file.
     *
     * @param file file to load the config file from.
     * @return ConfigManager object
     */
    private static ModConfig loadConfigFile(File file) {
        ModConfig config = null;

        if (file.exists()) {
            // An existing config is present, we should use its values
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                // Parses the config file and puts the values into config object
                config = GSON.fromJson(fileReader, ModConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Problem occurred when trying to load config: ", e);
            }
        }
        // gson.fromJson() can return null if file is empty
        if (config == null) {
            config = new ModConfig();
        }

        // Saves the file in order to write new fields if they were added
        config.saveConfigFile(file);
        return config;
    }

    /**
     * Saves the config to the given file.
     *
     * @param file file to save config to
     */
    private void saveConfigFile(File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
