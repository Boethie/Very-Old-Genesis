package genesis.common;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public final class GenesisConfig
{
	public static Configuration config;
	public static int flintAndMarcasiteMaxDamage = 33;
	
	//biomes
	public static int rainforestId = 50;
	public static int rainforestWeight = 10;
	public static int rainforestEdgeId = 51;
	public static int riverId = 52;
	public static int shallowOceanId = 53;
	public static int auxForestId = 54;
	public static int auxForestWeight = 10;
	public static int auxForestEdgeId = 55;
	public static int limestoneBeachId = 56;
	public static int swampRainForestId = 57;
	public static int swampRainForestWeight = 10;
	public static int archaeopterisForestId = 58;
	public static int archaeopterisForestWeight = 10;
	public static int auxPlainsId = 59;
	public static int auxPlainsWeight = 10;
	public static int archaeopterisPlainsId = 60;
	public static int archaeopterisPlainsWeight = 10;
	public static int genesisBeachId = 61;
	
	//dimensions
	public static int genesisDimId = 37;
	public static int genesisProviderId = 37;

	public static void readConfigValues(File configFile)
	{
		config = new Configuration(configFile);
		config.load();
		
		rainforestId = config.getInt("rainforestId", "biome", rainforestId, 0, 255, "Rainforest Biome ID");
		rainforestWeight = config.getInt("rainforestWeight", "biome", rainforestWeight, 0, Integer.MAX_VALUE, "Rainforest Biome Weight");
		rainforestEdgeId = config.getInt("rainforestEdgeId", "biome", rainforestEdgeId, 0, 255, "Rainforest Edge Biome ID");
		riverId = config.getInt("riverId", "biome", riverId, 0, 255, "River Biome ID");
		shallowOceanId = config.getInt("shallowOceanId", "biome", shallowOceanId, 0, 255, "Shallow Ocean Biome ID");
		auxForestId = config.getInt("auxForestId", "biome", auxForestId, 0, 255, "Araucarioxylon Forest Biome ID");
		auxForestWeight = config.getInt("auxForestWeight", "biome", auxForestWeight, 0, Integer.MAX_VALUE, "Araucarioxylon Forest Biome Weight");
		auxForestEdgeId = config.getInt("auxForestEdgeId", "biome", auxForestEdgeId, 0, 255, "Araucarioxylon Forest Edge Biome ID");
		
		genesisDimId = config.getInt("genesisDimId", "dimension", genesisDimId, Integer.MIN_VALUE, Integer.MAX_VALUE, "Genesis Dimension ID");
		genesisProviderId = config.getInt("genesisProviderId", "dimension", genesisProviderId, Integer.MIN_VALUE, Integer.MAX_VALUE, "Genesis Provider ID");
		
		flintAndMarcasiteMaxDamage = config.get("tool", "flintAndMarcasiteMaxDamage", flintAndMarcasiteMaxDamage).getInt();
		config.save();
	}
}
