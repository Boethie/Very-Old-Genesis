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
	public static final BiomeGenBase.Height heightDefault = new BiomeGenBase.Height(0.1F, 0.2F);
	public static final BiomeGenBase.Height heightShallowWaters = new BiomeGenBase.Height(-0.6F, 0.0F);
	public static final BiomeGenBase.Height heightShallowOceans = new BiomeGenBase.Height(-0.7F, 0.1F);
	public static final BiomeGenBase.Height heightOceans = new BiomeGenBase.Height(-1.0F, 0.1F);
	public static final BiomeGenBase.Height heightDeepOceans = new BiomeGenBase.Height(-1.8F, 0.1F);
	public static final BiomeGenBase.Height heightLowPlains = new BiomeGenBase.Height(0.125F, 0.05F);
	public static final BiomeGenBase.Height heightMidPlains = new BiomeGenBase.Height(0.2F, 0.2F);
	public static final BiomeGenBase.Height heightLowHills = new BiomeGenBase.Height(0.45F, 0.3F);
	public static final BiomeGenBase.Height heightHighPlateaus = new BiomeGenBase.Height(1.5F, 0.025F);
	public static final BiomeGenBase.Height heightMidHills = new BiomeGenBase.Height(1.0F, 0.5F);
	public static final BiomeGenBase.Height heightShores = new BiomeGenBase.Height(0.0F, 0.025F);
	public static final BiomeGenBase.Height heightRockyWaters = new BiomeGenBase.Height(0.1F, 0.8F);
	public static final BiomeGenBase.Height heightLowIslands = new BiomeGenBase.Height(0.2F, 0.3F);
	public static final BiomeGenBase.Height heightPartiallySubmerged = new BiomeGenBase.Height(-0.2F, 0.1F);
	public static final BiomeGenBase.Height heightHighHills = new BiomeGenBase.Height(2.5F, 0.5F);
	public static final BiomeGenBase.Height heightMidPlateaus = new BiomeGenBase.Height(1.5F, 0.025F);
	public static final BiomeGenBase.Height heightSeaPlateaus = new BiomeGenBase.Height(0.0F, 0.025F);
	public static final BiomeGenBase.Height heightLowPlateaus = new BiomeGenBase.Height(0.5F, 0.025F);
	public static final BiomeGenBase.Height heightHighPlains = new BiomeGenBase.Height(0.5F, 0.1F);
	public static final BiomeGenBase.Height heightPSPlateaus = new BiomeGenBase.Height(-0.2F, 0.05F);

	public static int rainforestId;
	public static BiomeGenBase rainforest;

	public static int rainforestEdgeId;
	public static BiomeGenGenesisBase rainforestEdge;

	public static int riverId;
	public static BiomeGenGenesisBase river;

	public static int rainforestSwampId;
	public static BiomeGenGenesisBase rainforestSwamp;

	public static int araucarioxylonForestId;
	public static BiomeGenGenesisBase araucarioxylonForest;
	
	public static int oceanId;
	public static BiomeGenGenesisBase ocean;

	public static void config() {
		File configFile = new File(Genesis.configFolder, "Biomes.cfg");
		Configuration config = new Configuration(configFile);
		config.load();

		rainforestEdgeId = config.getInt("Genesis Rainforest Edge Biome ID:", Configuration.CATEGORY_GENERAL, 123, 0, 255, "Biome ID for the Genesis Rainforest Edge");
		riverId = config.getInt("Genesis River Biome ID:", Configuration.CATEGORY_GENERAL, 121, 0, 255, "Biome ID for the Genesis River");
		araucarioxylonForestId = config.getInt("Araucarioxylon Forest Biome ID:", Configuration.CATEGORY_GENERAL, 124, 0, 255, "Biome ID for the Araucarioxylon Forest");
		rainforestId = config.getInt("Genesis Rainforest Biome ID:", Configuration.CATEGORY_GENERAL, 120, 0, 255, "Biome ID for the Genesis Rainforest");
		rainforestSwampId = config.getInt("Genesis Rainforest Swamp Biome ID:", Configuration.CATEGORY_GENERAL, 122, 0, 255, "Biome ID for the Genesis Rainforest Swamp");
		oceanId = config.getInt("Genesis Ocean Biome ID:", Configuration.CATEGORY_GENERAL, 125, 0, 255, "Biome ID for the Genesis Ocean");

		config.save();
	}

	public static void loadBiomes() {
		rainforestEdge = (BiomeGenGenesisBase) (new BiomeGenGenesisRainforestEdge(rainforestEdgeId)).setColor(9286496).setTemperatureRainfall(0.9F, 0.8F).setHeight(heightLowHills).setBiomeName("Genesis Rainforest Edge");
		river = (BiomeGenGenesisBase) (new BiomeGenGenesisRiver(riverId)).setColor(255).setTemperatureRainfall(0.7F, 1.0F).setHeight(heightShallowWaters).setBiomeName("Genesis River");
		araucarioxylonForest = (BiomeGenGenesisBase) (new BiomeGenGenesisAuxForest(araucarioxylonForestId)).setColor(3423968).setTemperatureRainfall(0.9F, 0.8F).setHeight(heightLowHills).setBiomeName("Araucarioxylon Forest");
		rainforest = (new BiomeGenGenesisRainforest(rainforestId)).setColor(9286496).setTemperatureRainfall(0.9F, 0.8F).setHeight(heightLowHills).setBiomeName("Genesis Rainforest");
		rainforestSwamp = (BiomeGenGenesisBase) (new BiomeGenGenesisRainforestSwamp(rainforestSwampId)).setColor(9286496).setTemperatureRainfall(0.7F, 1.0F).setHeight(heightPSPlateaus).setBiomeName("Genesis Rainforest Swamp");
		ocean = (BiomeGenGenesisBase) (new BiomeGenGenesisOcean(oceanId)).setColor(9286496).setTemperatureRainfall(0.7F, 1.0F).setHeight(heightShallowOceans).setBiomeName("Genesis Ocean");
	}
}
