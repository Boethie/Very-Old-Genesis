package genesis.common;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public final class GenesisConfig
{
	public static Configuration config;
	public static int flintAndMarcasiteMaxDamage = 33;
	public static int rainforestId = 50;

	public static void readConfigValues(File configFile)
	{
		config = new Configuration(configFile);
		config.load();
		rainforestId = config.getInt("rainforestId", "biome", rainforestId, 0, 255, "Rainforest Biome ID");
		flintAndMarcasiteMaxDamage = config.get("tool", "flintAndMarcasiteMaxDamage", flintAndMarcasiteMaxDamage).getInt();
		config.save();
	}
}
