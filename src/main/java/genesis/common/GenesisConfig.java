package genesis.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class GenesisConfig {
    public static int flintAndMarcasiteMaxDamage = 33;

    private static Configuration config;

    public static Configuration getConfig() {
        return config;
    }

    protected static void readConfigValues(File configFile) {
        config = new Configuration(configFile);
        config.load();
        config.get("tool", "flintAndMarcasiteMaxDamage", flintAndMarcasiteMaxDamage).getInt();
        config.save();
    }
}
