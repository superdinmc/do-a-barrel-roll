package nl.enjarai.doabarrelroll.compat.midnightcontrols;

import nl.enjarai.cicada.api.compat.CompatMixinPlugin;

import java.util.Set;

public class MidnightControlsPlugin implements CompatMixinPlugin {
    @Override
    public Set<String> getRequiredMods() {
        return Set.of("midnightcontrols");
    }
}
