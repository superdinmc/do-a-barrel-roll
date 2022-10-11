package nl.enjarai.doabarrelroll.moonlightconfigs.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import nl.enjarai.doabarrelroll.moonlightconfigs.cloth_config.ClothConfigCompat;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigBuilder;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigSpec;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigType;
import nl.enjarai.doabarrelroll.moonlightconfigs.yacl.YACLCompat;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FabricConfigSpec extends ConfigSpec {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Identifier res;
    private final ConfigSubCategory mainEntry;
    private final File file;

    public FabricConfigSpec(Identifier name, ConfigSubCategory mainEntry, ConfigType type, boolean synced, Runnable changeCallback) {
        super(name, FabricLoader.getInstance().getConfigDir(), type, synced, changeCallback);
        this.file = this.getFullPath().toFile();
        this.mainEntry = mainEntry;
        this.res = name;
    }

    public ConfigSubCategory getMainEntry() {
        return mainEntry;
    }

    @Override
    public void register() {
        FabricConfigSpec.addTrackedSpec(this);
    }

    @Override
    public void loadFromFile() {
        JsonElement config = null;

        if (file.exists() && file.isFile()) {
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                config = GSON.fromJson(bufferedReader, JsonElement.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config", e);
            }
        }

        if (config instanceof JsonObject jo) {
            //dont call load directly so we skip the main category name
            mainEntry.getEntries().forEach(e -> e.loadFromJson(jo));
        }
    }

    public void saveConfig() {
        try (FileOutputStream stream = new FileOutputStream(this.file);
             Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {

            JsonObject jo = new JsonObject();
            mainEntry.getEntries().forEach(e -> e.saveToJson(jo));

            GSON.toJson(jo, writer);
        } catch (IOException ignored) {
        }
        this.onRefresh();
    }

    public Text getName() {
        return Text.literal(ConfigBuilder.getReadableName(this.res.getPath() + "_configs"));
    }

    private static final boolean YACL = FabricLoader.getInstance().isModLoaded("yet-another-config-lib");
    private static final boolean clothConfig = FabricLoader.getInstance().isModLoaded("cloth_config")
            || FabricLoader.getInstance().isModLoaded("cloth_config2");

    @Override
    @Environment(EnvType.CLIENT)
    public Screen makeScreen(Screen parent, Identifier background) {
        if (YACL) {
            return YACLCompat.makeScreen(parent, this, background);
        } else if (clothConfig) {
            return ClothConfigCompat.makeScreen(parent, this, background);
        }
        return null;
    }

    @Override
    public boolean hasConfigScreen() {
        return clothConfig || YACL;
    }

    @Override
    public void loadFromBytes(InputStream stream) {
        InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        JsonElement config = GSON.fromJson(bufferedReader, JsonElement.class);
        if (config instanceof JsonObject jo) {
            //don't call load directly, so we skip the main category name
            mainEntry.getEntries().forEach(e -> e.loadFromJson(jo));
        }
        this.onRefresh();
    }
}
