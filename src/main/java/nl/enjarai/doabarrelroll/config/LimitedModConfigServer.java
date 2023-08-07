package nl.enjarai.doabarrelroll.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public interface LimitedModConfigServer {
    Codec<LimitedModConfigServer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("allowThrusting", ModConfigServer.DEFAULT.allowThrusting()).forGetter(LimitedModConfigServer::allowThrusting),
            Codec.BOOL.optionalFieldOf("forceEnabled", ModConfigServer.DEFAULT.forceEnabled()).forGetter(LimitedModConfigServer::forceEnabled)
    ).apply(instance, Impl::new));

    boolean allowThrusting();

    boolean forceEnabled();

    default Optional<ModConfigServer> tryToFull() {
        return this instanceof ModConfigServer full ? Optional.of(full) : Optional.empty();
    }

    record Impl(boolean allowThrusting, boolean forceEnabled) implements LimitedModConfigServer {
    }
}
