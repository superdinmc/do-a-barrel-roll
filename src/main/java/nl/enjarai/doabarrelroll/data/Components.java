package nl.enjarai.doabarrelroll.data;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import org.jetbrains.annotations.NotNull;

public class Components implements EntityComponentInitializer {
    public static final ComponentKey<RollComponent> ROLL =
            ComponentRegistry.getOrCreate(DoABarrelRoll.id("roll"), RollComponent.class);

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ROLL, player -> new RollComponentImpl(), RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
