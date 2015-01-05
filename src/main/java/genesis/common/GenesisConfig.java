package genesis.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class GenesisConfig {
    private static Configuration config;

    public static Configuration getConfig() {
        return config;
    }

    protected static void readConfigValues(File configFile) {
        config = new Configuration(configFile);
        //config.load();
        //config.save();
    }
}
