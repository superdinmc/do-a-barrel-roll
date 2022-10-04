package nl.enjarai.doabarrelroll.compat.lambdacontrols;

import nl.enjarai.doabarrelroll.compat.CompatMixinPlugin;

import java.util.Set;

public class LambdaControlsPlugin implements CompatMixinPlugin {
    @Override
    public Set<String> getRequiredMods() {
        return Set.of("lambdacontrols");
    }
}
