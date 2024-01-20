package nl.enjarai.doabarrelroll.compat.midnightcontrols;

import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import eu.midnightdust.midnightcontrols.client.controller.InputManager;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.ModKeybindings;
import nl.enjarai.doabarrelroll.api.event.RollContext;
import nl.enjarai.doabarrelroll.api.event.RollEvents;
import nl.enjarai.doabarrelroll.api.event.ThrustEvents;
import nl.enjarai.doabarrelroll.api.rotation.RotationInstant;
import nl.enjarai.doabarrelroll.compat.midnightcontrols.mixin.ButtonBindingAccessor;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.config.Sensitivity;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LAST;

public class MidnightControlsCompat {

	public static final Int2FloatMap AXIS_VALUES = new Int2FloatOpenHashMap(GLFW_GAMEPAD_AXIS_LAST);

	public static ButtonBinding PITCH_UP;
	public static ButtonBinding PITCH_DOWN;
	public static ButtonBinding ROLL_LEFT;
	public static ButtonBinding ROLL_RIGHT;
	public static ButtonBinding YAW_LEFT;
	public static ButtonBinding YAW_RIGHT;
	public static ButtonBinding THRUST_FORWARD;
	public static ButtonBinding THRUST_BACKWARD;

	private RotationInstant applyToRotation(RotationInstant rotationDelta, RollContext context) {
		Sensitivity sensitivity = ModConfig.INSTANCE.getControllerSensitivity();

		double multiplier = context.getRenderDelta() * 1200;

        double pitchAxis = Math.pow(AXIS_VALUES.getOrDefault(PITCH_DOWN.getButton()[0], InputManager.BUTTON_VALUES.getOrDefault(PITCH_DOWN.getButton()[0], 0f)), 2f) -
                Math.pow(AXIS_VALUES.getOrDefault(PITCH_UP.getButton()[0], InputManager.BUTTON_VALUES.getOrDefault(PITCH_UP.getButton()[0], 0f)), 2f);
        double yawAxis = Math.pow(AXIS_VALUES.getOrDefault(YAW_RIGHT.getButton()[0], InputManager.BUTTON_VALUES.getOrDefault(YAW_RIGHT.getButton()[0], 0f)), 2f) -
                Math.pow(AXIS_VALUES.getOrDefault(YAW_LEFT.getButton()[0], InputManager.BUTTON_VALUES.getOrDefault(YAW_LEFT.getButton()[0], 0f)), 2f);
        double rollAxis = Math.pow(AXIS_VALUES.getOrDefault(ROLL_RIGHT.getButton()[0], InputManager.BUTTON_VALUES.getOrDefault(ROLL_RIGHT.getButton()[0], 0f)), 2f) -
                Math.pow(AXIS_VALUES.getOrDefault(ROLL_LEFT.getButton()[0], InputManager.BUTTON_VALUES.getOrDefault(ROLL_LEFT.getButton()[0], 0f)), 2f);

		pitchAxis *= multiplier * sensitivity.pitch;
		yawAxis *= multiplier * sensitivity.yaw;
		rollAxis *= multiplier * sensitivity.roll;

		return rotationDelta.add(pitchAxis, yawAxis, rollAxis);
	}

	public static double getThrustModifier() {
		float forward = InputManager.BUTTON_VALUES.getOrDefault(THRUST_FORWARD.getButton()[0], 0f);
		float backward = InputManager.BUTTON_VALUES.getOrDefault(THRUST_BACKWARD.getButton()[0], 0f);
		return forward - backward;
	}

	public MidnightControlsCompat() {
		HashMap<String, ButtonBinding> bindings = new HashMap<>();
		InputManager.streamCategories()
				.filter(c -> c.getIdentifier().equals(new org.aperlambda.lambdacommon.Identifier("minecraft", ModKeybindings.PITCH_UP.getCategory())))
				.findFirst().ifPresent(list -> list.getBindings().forEach(bind -> bindings.put(bind.getName(), bind)));

		PITCH_UP = bindings.get(ModKeybindings.PITCH_UP.getTranslationKey());
		((ButtonBindingAccessor)PITCH_UP).getActions().clear();
		PITCH_DOWN = bindings.get(ModKeybindings.PITCH_DOWN.getTranslationKey());
		((ButtonBindingAccessor)PITCH_DOWN).getActions().clear();
		ROLL_LEFT = bindings.get(ModKeybindings.ROLL_LEFT.getTranslationKey());
		((ButtonBindingAccessor)ROLL_LEFT).getActions().clear();
		ROLL_RIGHT = bindings.get(ModKeybindings.ROLL_RIGHT.getTranslationKey());
		((ButtonBindingAccessor)ROLL_RIGHT).getActions().clear();
		YAW_LEFT = bindings.get(ModKeybindings.YAW_LEFT.getTranslationKey());
		((ButtonBindingAccessor)YAW_LEFT).getActions().clear();
		YAW_RIGHT = bindings.get(ModKeybindings.YAW_RIGHT.getTranslationKey());
		((ButtonBindingAccessor)YAW_RIGHT).getActions().clear();
		THRUST_FORWARD = bindings.get(ModKeybindings.THRUST_FORWARD.getTranslationKey());
		((ButtonBindingAccessor)THRUST_FORWARD).getActions().clear();
		THRUST_BACKWARD = bindings.get(ModKeybindings.THRUST_BACKWARD.getTranslationKey());
		((ButtonBindingAccessor)THRUST_BACKWARD).getActions().clear();

		RollEvents.LATE_CAMERA_MODIFIERS.register(context -> context
						.useModifier(this::applyToRotation),
				5, DoABarrelRollClient::isFallFlying);

		ThrustEvents.MODIFY_THRUST_INPUT.register(input -> input + getThrustModifier());
	}

}
