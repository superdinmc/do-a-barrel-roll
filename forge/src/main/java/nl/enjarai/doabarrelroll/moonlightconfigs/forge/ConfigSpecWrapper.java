package nl.enjarai.doabarrelroll.moonlightconfigs.forge;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigSpec;
import nl.enjarai.doabarrelroll.moonlightconfigs.ConfigType;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;

public class ConfigSpecWrapper extends ConfigSpec {

    private final ForgeConfigSpec spec;

    private final ModConfig modConfig;

    public ConfigSpecWrapper(Identifier name, ForgeConfigSpec spec, ConfigType type, boolean synced, @javax.annotation.Nullable Runnable onChange) {
        super(name, FMLPaths.CONFIGDIR.get(), type, synced, onChange);
        this.spec = spec;

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (onChange != null) bus.addListener(this::onConfigChange);

        ModConfig.Type t = this.getConfigType() == ConfigType.COMMON ? ModConfig.Type.COMMON : ModConfig.Type.CLIENT;

        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();
        this.modConfig = new ModConfig(t, spec, modContainer, name.getNamespace() + "-" + name.getPath() + ".toml");
        //for event
        ConfigSpec.addTrackedSpec(this);
    }

    @Override
    public String getFileName() {
        return modConfig.getFileName();
    }

    @Override
    public Path getFullPath() {
        return FMLPaths.CONFIGDIR.get().resolve(this.getFileName());
        // return modConfig.getFullPath();
    }

    @Override
    public void register() {
        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();
        modContainer.addConfig(this.modConfig);
    }

    @Override
    public void loadFromFile() {
        CommentedFileConfig replacementConfig = CommentedFileConfig
                .builder(this.getFullPath())
                .sync()
                .preserveInsertionOrder()
                .writingMode(WritingMode.REPLACE)
                .build();
        replacementConfig.load();
        replacementConfig.save();

        spec.setConfig(replacementConfig);
    }

    public ForgeConfigSpec getSpec() {
        return spec;
    }

    @Nullable
    public ModConfig getModConfig() {
        return modConfig;
    }

    public ModConfig.Type getModConfigType() {
        return this.getConfigType() == ConfigType.CLIENT ? ModConfig.Type.CLIENT : ModConfig.Type.COMMON;
    }

    @Override
    public boolean isLoaded() {
        return spec.isLoaded();
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public Screen makeScreen(Screen parent, @Nullable Identifier background) {
        var container = ModList.get().getModContainerById(this.getModId());
        if (container.isPresent()) {
            var factory = container.get().getCustomExtension(ConfigGuiHandler.ConfigGuiFactory.class);
            if (factory.isPresent()) return factory.get().screenFunction().apply(MinecraftClient.getInstance(), parent);
        }
        return null;
    }

    @Override
    public boolean hasConfigScreen() {
        return ModList.get().getModContainerById(this.getModId())
                .map(container -> container.getCustomExtension(ConfigGuiHandler.ConfigGuiFactory.class)
                        .isPresent()).orElse(false);
    }

    protected void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == this.getSpec()) {
            //send this configuration to connected clients
            onRefresh();
        }
    }

    @Override
    public void loadFromBytes(InputStream stream) {
        // try { //this should work the same as below
        //      var b = stream.readAllBytes();
        //     this.modConfig.acceptSyncedConfig(b);
        // } catch (Exception ignored) {
        // }

        //using this isntead so we dont fire the config changes event otherwise this will loop
        this.getSpec().setConfig(TomlFormat.instance().createParser().parse(stream));
        this.onRefresh();
    }


}
