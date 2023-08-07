package nl.enjarai.doabarrelroll.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.net.SyncableConfig;
import nl.enjarai.doabarrelroll.net.ValidatableConfig;

public record ModConfigServer(boolean allowThrusting,
                              boolean forceEnabled,
                              boolean forceInstalled,
                              int installedTimeout) implements SyncableConfig<ModConfigServer>, LimitedModConfigServer, ValidatableConfig {
    public static final ModConfigServer DEFAULT = new ModConfigServer(false, false, false, 20);
    public static final Codec<ModConfigServer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("allowThrusting", DEFAULT.allowThrusting()).forGetter(ModConfigServer::allowThrusting),
            Codec.BOOL.optionalFieldOf("forceEnabled", DEFAULT.forceEnabled()).forGetter(ModConfigServer::forceEnabled),
            Codec.BOOL.optionalFieldOf("forceInstalled", DEFAULT.forceInstalled()).forGetter(ModConfigServer::forceInstalled),
            Codec.INT.optionalFieldOf("installedTimeout", DEFAULT.installedTimeout()).forGetter(ModConfigServer::installedTimeout)
    ).apply(instance, ModConfigServer::new));

    @Override
    public Integer getSyncTimeout() {
        return forceInstalled ? installedTimeout : null;
    }

    @Override
    public Text getSyncTimeoutMessage() {
        return Text.of("Please install Do a Barrel Roll 2.4.0 or later to play on this server.");
    }

    public static boolean canModify(ServerPlayerEntity player) {
        return Permissions.check(player, DoABarrelRoll.MODID + ".configure", 3);
    }

    @Override
    public boolean isValid() {
        return installedTimeout > 0;
    }
}
