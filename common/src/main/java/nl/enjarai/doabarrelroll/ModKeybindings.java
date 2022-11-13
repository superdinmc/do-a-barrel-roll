package nl.enjarai.doabarrelroll;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ModKeybindings {

    public static final KeyBinding TOGGLE_ENABLED = new KeyBinding(
            "key.do_a_barrel_roll.toggle_enabled",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            "category.do_a_barrel_roll.do_a_barrel_roll"
    );

    public static final List<KeyBinding> ALL = List.of(
            TOGGLE_ENABLED
    );
}
