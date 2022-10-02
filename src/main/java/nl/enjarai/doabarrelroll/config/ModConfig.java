package nl.enjarai.doabarrelroll.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;

@Config(name = DoABarrelRollClient.MODID)
public class ModConfig implements ConfigData, ConfiguresRotation {
    @ConfigEntry.Gui.Excluded
    public static ModConfig INSTANCE;

    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public boolean modEnabled = true;

    @ConfigEntry.Gui.Tooltip
    public boolean switchRollAndYaw = false;

    @ConfigEntry.Gui.Tooltip
    public boolean momentumBasedMouse = false;

    @ConfigEntry.Gui.Tooltip
    public boolean showMomentumWidget = true;

    public boolean invertPitch = false;

    @ConfigEntry.Gui.Tooltip
    public boolean enableBanking = true;

    public float bankingStrength = 20;

    @ConfigEntry.Gui.Tooltip(count = 4)
    public boolean enableThrust = false;

    @ConfigEntry.Gui.Tooltip
    public float maxThrust = 1;

    @ConfigEntry.Gui.CollapsibleObject
    public Sensitivity desktopSensitivity = new Sensitivity();

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    public Sensitivity controllerSensitivity = new Sensitivity();


    @Override
    public RotationInstant configureRotation(RotationInstant rotationInstant) {
        var pitch = rotationInstant.getPitch();
        var yaw = rotationInstant.getYaw();
        var roll = rotationInstant.getRoll();

        if (switchRollAndYaw) {
            var temp = yaw;
            yaw = roll;
            roll = temp;
        }
        if (invertPitch) {
            pitch = -pitch;
        }

        return new RotationInstant(pitch, yaw, roll, rotationInstant.getRenderDelta());
    }
}
