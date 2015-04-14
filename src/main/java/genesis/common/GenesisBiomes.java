package genesis.common;

import java.io.File;

import genesis.world.biome.BiomeGenGenesisAuxForest;
import genesis.world.biome.BiomeGenGenesisBase;
import genesis.world.biome.BiomeGenGenesisOcean;
import genesis.world.biome.BiomeGenGenesisRainforest;
import genesis.world.biome.BiomeGenGenesisRainforestEdge;
import genesis.world.biome.BiomeGenGenesisRainforestSwamp;
import genesis.world.biome.BiomeGenGenesisRiver;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public final class GenesisBiomes
{
	public static final BiomeGenBase.Height DEFAULT = new BiomeGenBase.Height(0.1F, 0.2F);
	public static final BiomeGenBase.Height HIGH_MOUNTAINS = new BiomeGenBase.Height(2.5F, 0.5F);
	public static final BiomeGenBase.Height MID_PLATEAUS = new BiomeGenBase.Height(1.5F, 0.025F);
	public static final BiomeGenBase.Height LOW_PLATEAUS = new BiomeGenBase.Height(0.5F, 0.025F);
	public static final BiomeGenBase.Height MID_HILLS = new BiomeGenBase.Height(1.0F, 0.5F);
	public static final BiomeGenBase.Height LOW_HILLS = new BiomeGenBase.Height(0.45F, 0.3F);
	public static final BiomeGenBase.Height HIGH_PLAINS = new BiomeGenBase.Height(0.5F, 0.1F);
	public static final BiomeGenBase.Height MID_PLAINS = new BiomeGenBase.Height(0.2F, 0.2F);
	public static final BiomeGenBase.Height LOW_PLAINS = new BiomeGenBase.Height(0.125F, 0.05F);
	public static final BiomeGenBase.Height LOW_ISLANDS = new BiomeGenBase.Height(0.2F, 0.3F);
	public static final BiomeGenBase.Height ROCKY_WATERS = new BiomeGenBase.Height(0.1F, 0.8F);
	public static final BiomeGenBase.Height SHORES = new BiomeGenBase.Height(0.0F, 0.025F);
	public static final BiomeGenBase.Height SEA_PLATEAUS = new BiomeGenBase.Height(0.0F, 0.025F);
	public static final BiomeGenBase.Height SUBMERGED_PLATEAUS = new BiomeGenBase.Height(-0.2F, 0.05F);
	public static final BiomeGenBase.Height PARTIALLY_SUBMERGED = new BiomeGenBase.Height(-0.2F, 0.1F);
	public static final BiomeGenBase.Height SHALLOW_WATERS = new BiomeGenBase.Height(-0.6F, 0.0F);
	public static final BiomeGenBase.Height SHALLOW_OCEANS = new BiomeGenBase.Height(-0.7F, 0.1F);
	public static final BiomeGenBase.Height OCEANS = new BiomeGenBase.Height(-1.0F, 0.1F);
	public static final BiomeGenBase.Height DEEP_OCEANS = new BiomeGenBase.Height(-1.8F, 0.1F);

	public static int rainforestSwampId;
	public static BiomeGenGenesisBase rainforestSwamp;

	public static int rainforestId;
	public static BiomeGenBase rainforest;

	public static int rainforestEdgeId;
	public static BiomeGenGenesisBase rainforestEdge;

	public static int araucarioxylonForestId;
	public static BiomeGenGenesisBase araucarioxylonForest;
	
	public static int riverId;
	public static BiomeGenGenesisBase river;

	public static int shallowOceanId;
	public static BiomeGenGenesisBase shallowOcean;

	public static void config() {
		File configFile = new File(Genesis.configFolder, "Biomes.cfg");
		Configuration config = new Configuration(configFile);
		config.load();

		rainforestSwampId = config.getInt("Genesis Rainforest Swamp Biome ID:", Configuration.CATEGORY_GENERAL, 122, 0, 255, "Biome ID for the Genesis Rainforest Swamp");
		rainforestId = config.getInt("Genesis Rainforest Biome ID:", Configuration.CATEGORY_GENERAL, 120, 0, 255, "Biome ID for the Genesis Rainforest");
		rainforestEdgeId = config.getInt("Genesis Rainforest Edge Biome ID:", Configuration.CATEGORY_GENERAL, 123, 0, 255, "Biome ID for the Genesis Rainforest Edge");
		araucarioxylonForestId = config.getInt("Araucarioxylon Forest Biome ID:", Configuration.CATEGORY_GENERAL, 124, 0, 255, "Biome ID for the Araucarioxylon Forest");
		riverId = config.getInt("Genesis River Biome ID:", Configuration.CATEGORY_GENERAL, 121, 0, 255, "Biome ID for the Genesis River");
		shallowOceanId = config.getInt("Genesis Ocean Biome ID:", Configuration.CATEGORY_GENERAL, 125, 0, 255, "Biome ID for the Genesis Ocean");

		config.save();
	}

	public static void loadBiomes() {
		rainforestSwamp = (BiomeGenGenesisBase) (new BiomeGenGenesisRainforestSwamp(rainforestSwampId)).setColor(9286496).setTemperatureRainfall(0.7F, 1.0F).setHeight(SUBMERGED_PLATEAUS).setBiomeName("Rainforest Swamp");
		rainforest = (new BiomeGenGenesisRainforest(rainforestId)).setColor(9286496).setTemperatureRainfall(0.9F, 0.8F).setHeight(LOW_HILLS).setBiomeName("Rainforest");
		rainforestEdge = (BiomeGenGenesisBase) (new BiomeGenGenesisRainforestEdge(rainforestEdgeId)).setColor(9286496).setTemperatureRainfall(0.9F, 0.8F).setHeight(LOW_HILLS).setBiomeName("Rainforest Edge");
		araucarioxylonForest = (BiomeGenGenesisBase) (new BiomeGenGenesisAuxForest(araucarioxylonForestId)).setColor(3423968).setTemperatureRainfall(0.9F, 0.8F).setHeight(LOW_HILLS).setBiomeName("Araucarioxylon Forest");
		river = (BiomeGenGenesisBase) (new BiomeGenGenesisRiver(riverId)).setColor(255).setTemperatureRainfall(0.7F, 1.0F).setHeight(SHALLOW_WATERS).setBiomeName("River");
		shallowOcean = (BiomeGenGenesisBase) (new BiomeGenGenesisOcean(shallowOceanId)).setColor(9286496).setTemperatureRainfall(0.7F, 1.0F).setHeight(SHALLOW_OCEANS).setBiomeName("Shallow Ocean");
	}
}
