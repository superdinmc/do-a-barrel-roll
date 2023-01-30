package nl.enjarai.doabarrelroll.config;

public record SyncedModConfigClient(
        boolean allowThrusting,
        boolean forceEnabled
) implements SyncedModConfig {
}
