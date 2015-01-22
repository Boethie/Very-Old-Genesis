package genesis.common;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public final class GenesisConfig
{
	public static Configuration config;
	public static int flintAndMarcasiteMaxDamage = 33;

	public static void readConfigValues(File configFile)
	{
		config = new Configuration(configFile);
		config.load();
		config.get("tool", "flintAndMarcasiteMaxDamage", flintAndMarcasiteMaxDamage).getInt();
		config.save();
	}
}
