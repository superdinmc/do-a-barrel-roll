package nl.enjarai.doabarrelroll.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface SyncedModConfig {
    Codec<SyncedModConfig> TRANSFER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("allowThrusting", true).forGetter(SyncedModConfig::allowThrusting),
            Codec.BOOL.optionalFieldOf("forceEnabled", false).forGetter(SyncedModConfig::forceEnabled)
    ).apply(instance, SyncedModConfigClient::new));

    boolean allowThrusting();

    boolean forceEnabled();
}
