package nl.enjarai.doabarrelroll.compat.controlify;

import nl.enjarai.doabarrelroll.compat.CompatMixinPlugin;

import java.util.Set;

public class ControlifyPlugin implements CompatMixinPlugin {
    @Override
    public Set<String> getRequiredMods() {
        return Set.of("controlify");
    }
}
