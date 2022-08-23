package nl.enjarai.doabarrelroll.compat.midnightcontrols;

import eu.midnightdust.midnightcontrols.ControlsMode;
import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.MidnightControlsConfig;
import eu.midnightdust.midnightcontrols.client.compat.CompatHandler;
import eu.midnightdust.midnightcontrols.client.compat.MidnightControlsCompat;
import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import eu.midnightdust.midnightcontrols.client.controller.ButtonCategory;
import eu.midnightdust.midnightcontrols.client.controller.InputManager;
import net.minecraft.client.MinecraftClient;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import org.aperlambda.lambdacommon.Identifier;
import org.aperlambda.lambdacommon.utils.function.PairPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_X;

public class ControllerIntegration implements CompatHandler {
    public static final PairPredicate<MinecraftClient, ButtonBinding> USING_ELYTRA =
            (client, button) -> client.player != null && client.player.isFallFlying();

    public static final List<ButtonBinding> BINDINGS = new ArrayList<>();

    public static final ButtonBinding YAW = binding(new ButtonBinding.Builder(DoABarrelRollClient.id("yaw"))
            .buttons(ButtonBinding.axisAsButton(GLFW_GAMEPAD_AXIS_LEFT_X, true)).filter(USING_ELYTRA).register());

    public static final ButtonCategory CATEGORY =
            InputManager.registerCategory(new Identifier(DoABarrelRollClient.MODID, "do-a-barrel-roll"));
    //                                    There is 1 impostor among us

    public static Supplier<Boolean> ENABLED = () -> false;

    public static void init() {
        MidnightControlsCompat.registerCompatHandler(new ControllerIntegration());
    }

    @Override
    public void handle(@NotNull MidnightControlsClient midnightControlsClient) {
        CATEGORY.registerAllBindings(BINDINGS);
        ENABLED = () -> MidnightControlsConfig.controlsMode == ControlsMode.CONTROLLER;
    }

    private static ButtonBinding binding(ButtonBinding binding) {
        BINDINGS.add(binding);
        return binding;
    }
}
