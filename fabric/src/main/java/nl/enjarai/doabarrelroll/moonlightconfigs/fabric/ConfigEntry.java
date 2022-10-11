package nl.enjarai.doabarrelroll.moonlightconfigs.fabric;

import com.google.gson.JsonObject;
import net.minecraft.text.Text;

public abstract class ConfigEntry{

    protected final String name;
    private String translationKey;

    public ConfigEntry(String name) {
        this.name = name;
    }

    public abstract void loadFromJson(JsonObject object);

    public abstract void saveToJson(JsonObject object);

    public String getName() {
        return name;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public Text getTranslation() {
        return Text.translatable(translationKey);
    }
}