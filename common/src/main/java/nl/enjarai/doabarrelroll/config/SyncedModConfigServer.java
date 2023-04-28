package nl.enjarai.doabarrelroll.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.net.SyncableConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SyncedModConfigServer implements SyncableConfig<SyncedModConfigServer>, SyncedModConfig {
    public static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(DoABarrelRoll.MODID + "-server.json");
    public static SyncedModConfigServer INSTANCE;

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting() // Makes the json use new lines instead of being a "one-liner"
            .serializeNulls() // Makes fields with `null` value to be written as well.
            .disableHtmlEscaping() // We'll be able to use custom chars without them being saved differently
            .create();

    public boolean allowThrusting = true;
    public boolean forceEnabled = false;
    public boolean forceInstalled = false;
    public int installedTimeout = 20;

    public static void load() {
        INSTANCE = loadConfigFile(CONFIG_FILE);
    }

    public void save() {
        saveConfigFile(CONFIG_FILE);
    }

    /**
     * Loads config file.
     *
     * @param file file to load the config file from.
     * @return ConfigManager object
     */
    private static SyncedModConfigServer loadConfigFile(Path file) {
        SyncedModConfigServer config = null;

        if (Files.exists(file)) {
            // An existing config is present, we should use its values
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file.toFile()), StandardCharsets.UTF_8)
            )) {
                // Parses the config file and puts the values into config object
                config = GSON.fromJson(fileReader, SyncedModConfigServer.class);
            } catch (IOException e) {
                throw new RuntimeException("Problem occurred when trying to load config: ", e);
            }
        }
        // gson.fromJson() can return null if file is empty
        if (config == null) {
            config = new SyncedModConfigServer();
        }

        // Saves the file in order to write new fields if they were added
        config.saveConfigFile(file);
        return config;
    }

    /**
     * Saves the config to the given file.
     *
     * @param file file to save config to
     */
    private void saveConfigFile(Path file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file.toFile()), StandardCharsets.UTF_8)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getSyncTimeout() {
        return forceInstalled ? installedTimeout : null;
    }

    @Override
    public Text getSyncTimeoutMessage() {
        return new LiteralText("Please install Do a Barrel Roll 2.4.0 or later to play on this server.");
    }

    @Override
    public Codec<? super SyncedModConfigServer> getTransferCodec() {
        return SyncedModConfig.TRANSFER_CODEC;
    }

    @Override
    public boolean allowThrusting() {
        return allowThrusting;
    }

    @Override
    public boolean forceEnabled() {
        return forceEnabled;
    }
}
