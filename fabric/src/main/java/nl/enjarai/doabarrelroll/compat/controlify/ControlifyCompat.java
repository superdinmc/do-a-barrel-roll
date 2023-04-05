package nl.enjarai.doabarrelroll.compat.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControlifyBindingsApi;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.bindings.BindingSupplier;
import dev.isxander.controlify.bindings.GamepadBinds;
import net.minecraft.text.Text;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.api.event.RollEvents;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;

public class ControlifyCompat implements ControlifyEntrypoint {
    public static BindingSupplier PITCH_UP;
    public static BindingSupplier PITCH_DOWN;
    public static BindingSupplier ROLL_LEFT;
    public static BindingSupplier ROLL_RIGHT;
    public static BindingSupplier YAW_LEFT;
    public static BindingSupplier YAW_RIGHT;

    private RotationInstant applyToRotation(RotationInstant rotationDelta) {
        var controller = ControlifyApi.get().currentController();
        var sensitivity = ModConfig.INSTANCE.getControllerSensitivity();

        double multiplier = rotationDelta.getRenderDelta();

        float pitchAxis = PITCH_UP.get(controller).state() - PITCH_DOWN.get(controller).state();
        float yawAxis = YAW_RIGHT.get(controller).state() - YAW_LEFT.get(controller).state();
        float rollAxis = ROLL_RIGHT.get(controller).state() - ROLL_LEFT.get(controller).state();

        pitchAxis *= multiplier * sensitivity.pitch;
        yawAxis *= multiplier * sensitivity.yaw;
        rollAxis *= multiplier * sensitivity.roll;

        return rotationDelta.add(pitchAxis, yawAxis, rollAxis);
    }

    @Override
    public void onControlifyPreInit(ControlifyApi controlifyApi) {
        var bindings = ControlifyBindingsApi.get();
        PITCH_UP = bindings.registerBind(DoABarrelRoll.id("pitch_up"), builder -> builder
                .defaultBind(GamepadBinds.RIGHT_STICK_BACKWARD)
                .category(Text.translatable("controlify.category.do_a_barrel_roll.do_a_barrel_roll"))
                .name(Text.translatable("controlify.bind.do_a_barrel_roll.pitch_up"))
        );
        PITCH_DOWN = bindings.registerBind(DoABarrelRoll.id("pitch_down"), builder -> builder
                .defaultBind(GamepadBinds.RIGHT_STICK_FORWARD)
                .category(Text.translatable("controlify.category.do_a_barrel_roll.do_a_barrel_roll"))
                .name(Text.translatable("controlify.bind.do_a_barrel_roll.pitch_down"))
        );
        ROLL_LEFT = bindings.registerBind(DoABarrelRoll.id("roll_left"), builder -> builder
                .defaultBind(GamepadBinds.RIGHT_STICK_LEFT)
                .category(Text.translatable("controlify.category.do_a_barrel_roll.do_a_barrel_roll"))
                .name(Text.translatable("controlify.bind.do_a_barrel_roll.roll_left"))
        );
        ROLL_RIGHT = bindings.registerBind(DoABarrelRoll.id("roll_right"), builder -> builder
                .defaultBind(GamepadBinds.RIGHT_STICK_RIGHT)
                .category(Text.translatable("controlify.category.do_a_barrel_roll.do_a_barrel_roll"))
                .name(Text.translatable("controlify.bind.do_a_barrel_roll.roll_right"))
        );
        YAW_LEFT = bindings.registerBind(DoABarrelRoll.id("yaw_left"), builder -> builder
                .defaultBind(GamepadBinds.LEFT_STICK_LEFT)
                .category(Text.translatable("controlify.category.do_a_barrel_roll.do_a_barrel_roll"))
                .name(Text.translatable("controlify.bind.do_a_barrel_roll.yaw_left"))
        );
        YAW_RIGHT = bindings.registerBind(DoABarrelRoll.id("yaw_right"), builder -> builder
                .defaultBind(GamepadBinds.LEFT_STICK_RIGHT)
                .category(Text.translatable("controlify.category.do_a_barrel_roll.do_a_barrel_roll"))
                .name(Text.translatable("controlify.bind.do_a_barrel_roll.yaw_right"))
        );

        RollEvents.LATE_CAMERA_MODIFIERS.register((rotationDelta, currentRotation) -> rotationDelta
                .useModifier(this::applyToRotation),
                5, DoABarrelRollClient::isFallFlying);
    }

    @Override
    public void onControllersDiscovered(ControlifyApi controlifyApi) {
    }
}
