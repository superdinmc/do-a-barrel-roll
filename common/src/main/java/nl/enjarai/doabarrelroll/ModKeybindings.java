package nl.enjarai.doabarrelroll;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import nl.enjarai.doabarrelroll.config.ModConfig;
import nl.enjarai.doabarrelroll.config.SyncedModConfig;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ModKeybindings {

    public static final KeyBinding TOGGLE_ENABLED = new KeyBinding(
            "key.do_a_barrel_roll.toggle_enabled",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            "category.do_a_barrel_roll.do_a_barrel_roll"
    );
    public static final KeyBinding TOGGLE_THRUST = new KeyBinding(
            "key.do_a_barrel_roll.toggle_thrust",
            InputUtil.Type.KEYSYM,
            InputUtil.UNKNOWN_KEY.getCode(),
            "category.do_a_barrel_roll.do_a_barrel_roll"
    );

    public static final List<KeyBinding> FABRIC = List.of(
            TOGGLE_ENABLED,
            TOGGLE_THRUST
    );
    public static final List<KeyBinding> FORGE = List.of(
            TOGGLE_ENABLED
    );

    public static void clientTick(MinecraftClient client) {
        while (TOGGLE_ENABLED.wasPressed()) {
            if (!DoABarrelRollClient.HANDSHAKE_CLIENT.getConfig().map(SyncedModConfig::forceEnabled).orElse(false)) {
                ModConfig.INSTANCE.setModEnabled(!ModConfig.INSTANCE.getModEnabled());
                ModConfig.INSTANCE.save();

                if (client.player != null) {
                    client.player.sendMessage(
                            new TranslatableText(
                                    "key.do_a_barrel_roll." +
                                            (ModConfig.INSTANCE.getModEnabled() ? "toggle_enabled.enable" : "toggle_enabled.disable")
                            ),
                            true
                    );
                }
            } else {
                if (client.player != null) {
                    client.player.sendMessage(
                            new TranslatableText("key.do_a_barrel_roll.toggle_enabled.disallowed"),
                            true
                    );
                }
            }
        }
        while (TOGGLE_THRUST.wasPressed()) {
            if (DoABarrelRollClient.HANDSHAKE_CLIENT.getConfig().map(SyncedModConfig::allowThrusting).orElse(true)) {
                ModConfig.INSTANCE.setEnableThrust(!ModConfig.INSTANCE.getEnableThrust());
                ModConfig.INSTANCE.save();

                if (client.player != null) {
                    client.player.sendMessage(
                            new TranslatableText(
                                    "key.do_a_barrel_roll." +
                                            (ModConfig.INSTANCE.getEnableThrust() ? "toggle_thrust.enable" : "toggle_thrust.disable")
                            ),
                            true
                    );
                }
            } else {
                if (client.player != null) {
                    client.player.sendMessage(
                            new TranslatableText("key.do_a_barrel_roll.toggle_thrust.disallowed"),
                            true
                    );
                }
            }
        }
    }
}