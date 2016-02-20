package genesis.common;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public final class GenesisConfig
{
	public static Configuration config;
	public static int flintAndMarcasiteMaxDamage = 33;
	
	//Biomes
	public static int rainforestId = 50;
	public static int rainforestWeight = 10;
	public static int rainforestEdgeId = 51;
	public static int riverId = 52;
	public static int shallowOceanId = 53;
	public static int auxForestId = 54;
	public static int auxForestWeight = 10;
	public static int limestoneBeachId = 55;
	public static int swampRainForestId = 56;
	public static int swampRainForestWeight = 10;
	public static int floodplainsForestId = 57;
	public static int floodplainsForestWeight = 10;
	public static int genesisBeachId = 58;
	public static int rainforestHillsId = 59;
	public static int auxForestHillsId = 60;
	public static int deepOceanId = 61;
	public static int marshId = 62;
	public static int marshWeight = 10;
	public static int oceanId = 63;
	public static int redLowlandsId = 64;
	public static int redLowlandsWeight = 10;
	public static int redLowlandsHillsId = 65;
	public static int redBeachId = 66;
	public static int metaForestId = 67;
	public static int metaForestWeight = 10;
	public static int metaForestHillsId = 68;
	public static int savannaId = 69;
	public static int savannaWeight = 10;
	public static int woodlandsId = 70;
	public static int woodlandsWeight = 10;
	public static int woodlandsHillsId = 71;
	
	//Dimensions
	public static int genesisDimId = 37;
	public static int genesisProviderId = 37;
	
	//Ore gen
	public static int komatiiteCount = 62;
	public static int gneissCount = 20;
	public static int rhyoliteCount = 11;
	public static int doleriteCount = 11;
	public static int trondhjemiteCount = 8;
	public static int fauxCount = 5;
	public static int anorthositeCount = 5;
	public static int quartzCount = 52;
	public static int zirconCount = 17;
	public static int garnetCount = 13;
	public static int hematiteCount = 1;
	public static int manganeseCount = 4;
	public static int malachiteCount = 3;
	public static int azuriteCount = 2;
	public static int olivineCount = 1;
	public static int flintCount = 34;
	public static int marcasiteCount = 86;
	
	//Dimension music
	public static boolean playDimensionMusic = true;
	
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
		metaForestId = config.getInt("metaForestId", "biome", metaForestId, 0, 255, "Metasequoia Forest Biome ID");
		metaForestWeight = config.getInt("metaForestWeight", "biome", metaForestWeight, 0, Integer.MAX_VALUE, "Metasequoia Forest Biome Weight");
		redLowlandsId = config.getInt("redLowlandsId", "biome", redLowlandsId, 0, 255, "Red Lowlands Biome ID");
		redLowlandsWeight = config.getInt("redLowlandsWeight", "biome", redLowlandsWeight, 0, Integer.MAX_VALUE, "Red Lowlands Biome Weight");
		savannaId = config.getInt("savannaId", "biome", savannaId, 0, 255, "Savanna Biome ID");
		savannaWeight = config.getInt("savannaWeight", "biome", savannaWeight, 0, Integer.MAX_VALUE, "Savanna Biome Weight");
		woodlandsId = config.getInt("woodlandsId", "biome", woodlandsId, 0, 255, "Woodlands Biome Id");
		woodlandsWeight = config.getInt("woodlandsWeight", "biome", woodlandsWeight, 0, 255, "Woodlands Weight");
		
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
		
		playDimensionMusic = config.getBoolean("dimensionMusic", "music", true, "If true, new music will play in the dimension.");
		
		config.save();
	}
}
