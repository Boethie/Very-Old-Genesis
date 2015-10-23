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
	public static int floodplainsForestId = 58;
	public static int floodplainsForestWeight = 10;
	public static int auxPlainsId = 59;
	public static int auxPlainsWeight = 10;
	public static int genesisBeachId = 61;
	public static int rainforestHillsId = 62;
	public static int auxForestHillsId = 63;
	public static int deepOceanId = 65;
	public static int marshId = 66;
	public static int marshWeight = 10;
	public static int oceanId = 67;
	
	//dimensions
	public static int genesisDimId = 37;
	public static int genesisProviderId = 37;
	
	//Ore Gen
	public static int komatiiteCount = 62;
	public static int gneissCount = 20;
	public static int rhyoliteCount = 11;
	public static int doleriteCount = 11;
	public static int trondhjemiteCount = 8;
	public static int fauxCount = 5;
	public static int anorthositeCount = 5;
	public static int quartzCount = 74;
	public static int zirconCount = 31;
	public static int garnetCount = 27;
	public static int hematiteCount = 8;
	public static int manganeseCount = 6;
	public static int malachiteCount = 3;
	public static int azuriteCount = 3;
	public static int olivineCount = 2;
	public static int flintCount = 35;
	public static int marcasiteCount = 86;
	
	public static void readConfigValues(File configFile)
	{
		config = new Configuration(configFile);
		config.load();
		
		//Biome Ids
		rainforestId = config.getInt("rainforestId", "biome", rainforestId, 0, 255, "Rainforest Biome ID");
		rainforestWeight = config.getInt("rainforestWeight", "biome", rainforestWeight, 0, Integer.MAX_VALUE, "Rainforest Biome Weight");
		rainforestEdgeId = config.getInt("rainforestEdgeId", "biome", rainforestEdgeId, 0, 255, "Rainforest Edge Biome ID");
		riverId = config.getInt("riverId", "biome", riverId, 0, 255, "River Biome ID");
		shallowOceanId = config.getInt("shallowOceanId", "biome", shallowOceanId, 0, 255, "Shallow Ocean Biome ID");
		auxForestId = config.getInt("auxForestId", "biome", auxForestId, 0, 255, "Araucarioxylon Forest Biome ID");
		auxForestWeight = config.getInt("auxForestWeight", "biome", auxForestWeight, 0, Integer.MAX_VALUE, "Araucarioxylon Forest Biome Weight");
		auxForestEdgeId = config.getInt("auxForestEdgeId", "biome", auxForestEdgeId, 0, 255, "Araucarioxylon Forest Edge Biome ID");
		
		//Dimension Id
		genesisDimId = config.getInt("genesisDimId", "dimension", genesisDimId, Integer.MIN_VALUE, Integer.MAX_VALUE, "Genesis Dimension ID");
		genesisProviderId = config.getInt("genesisProviderId", "dimension", genesisProviderId, Integer.MIN_VALUE, Integer.MAX_VALUE, "Genesis Provider ID");
		
		//Tools
		flintAndMarcasiteMaxDamage = config.get("tool", "flintAndMarcasiteMaxDamage", flintAndMarcasiteMaxDamage).getInt();
		
		//Ore gen
		komatiiteCount = config.getInt("komatiite", "oregen", komatiiteCount, 0, 255, "Generation count for komatiite");
		gneissCount = config.getInt("gneiss", "oregen", gneissCount, 0, 255, "Generation count for gneiss");
		rhyoliteCount = config.getInt("rhyolite", "oregen", rhyoliteCount, 0, 255, "Generation count for rhyolite");
		doleriteCount = config.getInt("dolerite", "oregen", doleriteCount, 0, 255, "Generation count for dolerite");
		trondhjemiteCount = config.getInt("trondhjemite", "oregen", trondhjemiteCount, 0, 255, "Generation count for trondhjemite");
		fauxCount = config.getInt("faux", "oregen", fauxCount, 0, 255, "Generation count for faux");
		anorthositeCount = config.getInt("anorthosite", "oregen", anorthositeCount, 0, 255, "Generation count for anorthosite");
		quartzCount = config.getInt("quartz", "oregen", quartzCount, 0, 255, "Generation count for quartz");
		zirconCount = config.getInt("zircon", "oregen", zirconCount, 0, 255, "Generation count for zircon");
		garnetCount = config.getInt("garnet", "oregen", garnetCount, 0, 255, "Generation count for garnet");
		hematiteCount = config.getInt("hematite", "oregen", hematiteCount, 0, 255, "Generation count for hematite");
		manganeseCount = config.getInt("manganese", "oregen", manganeseCount, 0, 255, "Generation count for manganese");
		malachiteCount = config.getInt("malachite", "oregen", malachiteCount, 0, 255, "Generation count for malachite");
		azuriteCount = config.getInt("azurite", "oregen", azuriteCount, 0, 255, "Generation count for azurite");
		olivineCount = config.getInt("olivine", "oregen", olivineCount, 0, 255, "Generation count for olivine");
		flintCount = config.getInt("flint", "oregen", flintCount, 0, 255, "Generation count for flint");
		marcasiteCount = config.getInt("marcaiste", "oregen", marcasiteCount, 0, 255, "Generation count for marcaiste");
		
		config.save();
	}
}
