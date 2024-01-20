package nl.enjarai.doabarrelroll.compat.midnightcontrols.mixin;

import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import eu.midnightdust.midnightcontrols.client.controller.PressAction;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Pseudo
@Mixin(ButtonBinding.class)
public interface ButtonBindingAccessor {

	@Accessor("actions")
	List<PressAction> getActions();

}
