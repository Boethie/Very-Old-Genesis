package genesis.common;

import genesis.world.biome.BiomeGenAuxForest;
import genesis.world.biome.BiomeGenAuxForestM;
import genesis.world.biome.BiomeGenBaseGenesis;
import genesis.world.biome.BiomeGenBeachGenesis;
import genesis.world.biome.BiomeGenFloodplainsForest;
import genesis.world.biome.BiomeGenLimestoneBeach;
import genesis.world.biome.BiomeGenMarsh;
import genesis.world.biome.BiomeGenMetaForest;
import genesis.world.biome.BiomeGenMetaForestM;
import genesis.world.biome.BiomeGenOceanGenesis;
import genesis.world.biome.BiomeGenRainforest;
import genesis.world.biome.BiomeGenRainforestM;
import genesis.world.biome.BiomeGenRedBeach;
import genesis.world.biome.BiomeGenRiver;
import genesis.world.biome.BiomeGenShallowOcean;
import genesis.world.biome.BiomeGenSwampRainforest;
import genesis.world.biome.BiomeGenWoodlands;
import genesis.world.biome.BiomeGenWoodlandsM;
import genesis.world.biome.BiomeManagerGenesis;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeType;

public final class GenesisBiomes
{
	public static BiomeGenBaseGenesis rainforest;
	public static BiomeGenBaseGenesis rainforestM;
	public static BiomeGenBaseGenesis rainforestHills;
	public static BiomeGenBaseGenesis auxForest;
	public static BiomeGenBaseGenesis auxForestM;
	public static BiomeGenBaseGenesis auxForestHills;
	public static BiomeGenBaseGenesis metaForest;
	public static BiomeGenBaseGenesis metaForestM;
	public static BiomeGenBaseGenesis metaForestHills;
	public static BiomeGenBaseGenesis woodlands;
	public static BiomeGenBaseGenesis woodlandsM;
	public static BiomeGenBaseGenesis woodlandsHills;
	public static BiomeGenBaseGenesis swampRainForest;
	public static BiomeGenBaseGenesis marsh;
	public static BiomeGenBaseGenesis floodplainsForest;
	//public static BiomeGenBaseGenesis redLowlands;
	//public static BiomeGenBaseGenesis redLowlandsHills;
	//public static BiomeGenBaseGenesis redLowlandsM;
	public static BiomeGenBaseGenesis river;
	public static BiomeGenBaseGenesis shallowOcean;
	public static BiomeGenBaseGenesis ocean;
	public static BiomeGenBaseGenesis deepOcean;
	public static BiomeGenBaseGenesis genesisBeach;
	public static BiomeGenBaseGenesis redBeach;
	public static BiomeGenBaseGenesis limestoneBeach;
	/*
	public static final BiomeGenBase.Height height_ShallowWaters = new BiomeGenBase.Height(-0.5F, 0.0F);
	public static final BiomeGenBase.Height height_Oceans = new BiomeGenBase.Height(-1.0F, 0.1F);
	public static final BiomeGenBase.Height height_DeepOceans = new BiomeGenBase.Height(-1.8F, 0.1F);
	public static final BiomeGenBase.Height height_EmergingHills = new BiomeGenBase.Height(0.0F, 0.1F);
	public static final BiomeGenBase.Height height_Shore = new BiomeGenBase.Height(0.0F, 0.025F);
	*/
	public static void loadBiomes()
	{
		BiomeGenBase.BiomeProperties prop;
		
		prop = new BiomeGenBase.BiomeProperties("Rainforest");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(0.15F);
		prop.setHeightVariation(0.05F);
		prop.setWaterColor(0x725113);
		
		rainforest = new BiomeGenRainforest(prop);
		BiomeManagerGenesis.registerBiome(rainforest, GenesisConfig.rainforestId, BiomeType.WARM, GenesisConfig.rainforestWeight);
		BiomeDictionary.registerBiomeType(rainforest, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Rainforest M");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(2.2F);
		prop.setHeightVariation(0.4F);
		prop.setWaterColor(0x725113);
		
		rainforestM = new BiomeGenRainforestM(prop);
		rainforestM.setBiomeId(GenesisConfig.rainforestId + 128);
		BiomeDictionary.registerBiomeType(rainforestM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		
		prop = new BiomeGenBase.BiomeProperties("Rainforest Hills");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		rainforestHills = new BiomeGenRainforest(prop);
		rainforestHills.setBiomeId(GenesisConfig.rainforestHillsId);
		BiomeDictionary.registerBiomeType(rainforestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Araucarioxylon Forest");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.05F);
		prop.setHeightVariation(0.15F);
		prop.setWaterColor(0x725113);
		
		auxForest = new BiomeGenAuxForest(prop);
		BiomeManagerGenesis.registerBiome(auxForest, GenesisConfig.auxForestId, BiomeType.WARM, GenesisConfig.auxForestWeight);
		BiomeDictionary.registerBiomeType(auxForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Araucarioxylon Forest M");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(2.2F);
		prop.setHeightVariation(0.4F);
		prop.setWaterColor(0x725113);
		
		auxForestM = new BiomeGenAuxForestM(prop);
		auxForestM.setBiomeId(GenesisConfig.auxForestId + 128);
		BiomeDictionary.registerBiomeType(auxForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Araucarioxylon Forest Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		auxForestHills = new BiomeGenAuxForest(prop);
		auxForestHills.setBiomeId(GenesisConfig.auxForestHillsId);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Woodlands");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.035F);
		prop.setHeightVariation(0.135F);
		prop.setWaterColor(0x725113);
		
		woodlands = new BiomeGenWoodlands(prop);
		BiomeManagerGenesis.registerBiome(woodlands, GenesisConfig.woodlandsId, BiomeType.WARM, GenesisConfig.woodlandsWeight);
		BiomeDictionary.registerBiomeType(woodlands, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Woodlands M");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(2.2F);
		prop.setHeightVariation(0.4F);
		prop.setWaterColor(0x725113);
		
		woodlandsM = new BiomeGenWoodlandsM(prop);
		woodlandsM.setBiomeId(GenesisConfig.woodlandsId + 128);
		BiomeDictionary.registerBiomeType(woodlandsM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Woodlands Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		woodlandsHills = new BiomeGenWoodlands(prop);
		woodlandsHills.setBiomeId(GenesisConfig.woodlandsHillsId);
		BiomeDictionary.registerBiomeType(woodlandsHills, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Metasequoia Forest");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.04F);
		prop.setHeightVariation(0.165F);
		prop.setWaterColor(0x725113);
		
		metaForest = new BiomeGenMetaForest(prop);
		BiomeManagerGenesis.registerBiome(metaForest, GenesisConfig.metaForestId, BiomeType.WARM, GenesisConfig.metaForestWeight);
		BiomeDictionary.registerBiomeType(metaForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Metasequoia Forest M");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(2.2F);
		prop.setHeightVariation(0.4F);
		prop.setWaterColor(0x725113);
		
		metaForestM = new BiomeGenMetaForestM(prop);
		metaForestM.setBiomeId(GenesisConfig.metaForestId + 128);
		BiomeDictionary.registerBiomeType(metaForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Metasequoia Forest Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		metaForestHills = new BiomeGenMetaForest(prop);
		metaForestHills.setBiomeId(GenesisConfig.metaForestHillsId);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Swamp Rainforest");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(-0.2F);
		prop.setHeightVariation(0.03F);
		prop.setWaterColor(0x725113);
		
		swampRainForest = new BiomeGenSwampRainforest(prop);
		swampRainForest.setBiomeId(GenesisConfig.swampRainForestId);
		BiomeDictionary.registerBiomeType(swampRainForest, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Marsh");
		prop.setTemperature(1.15F);
		prop.setRainfall(0.3F);
		prop.setBaseHeight(0.0F);
		prop.setHeightVariation(-0.01F);
		prop.setWaterColor(0x725113);
		
		marsh = new BiomeGenMarsh(prop);
		BiomeManagerGenesis.registerBiome(marsh, GenesisConfig.marshId, BiomeType.WARM, GenesisConfig.marshWeight);
		BiomeDictionary.registerBiomeType(marsh, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		prop = new BiomeGenBase.BiomeProperties("Floodplains Forest");
		prop.setTemperature(1.15F);
		prop.setRainfall(1.0F);
		prop.setBaseHeight(-0.2F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x725113);
		
		floodplainsForest = new BiomeGenFloodplainsForest(prop);
		BiomeManagerGenesis.registerBiome(floodplainsForest, GenesisConfig.marshId, BiomeType.WARM, GenesisConfig.floodplainsForestWeight);
		BiomeDictionary.registerBiomeType(floodplainsForest, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		/*
		redLowlands = new BiomeGenRedLowlands(GenesisConfig.redLowlandsId);
		BiomeManagerGenesis.registerBiome(redLowlands, BiomeType.WARM, GenesisConfig.redLowlandsWeight);
		BiomeDictionary.registerBiomeType(redLowlands, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		
		redLowlandsHills = new BiomeGenRedLowlands(GenesisConfig.redLowlandsHillsId).setBiomeName("Red Lowlands Hills").setHeight(0.425F, 0.275F);
		BiomeDictionary.registerBiomeType(redLowlandsHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		
		redLowlandsM = new BiomeGenRedLowlandsM(GenesisConfig.redLowlandsId+128);
		BiomeDictionary.registerBiomeType(redLowlandsM, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		*/
		
		prop = new BiomeGenBase.BiomeProperties("River");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-0.5F);
		prop.setHeightVariation(0.0F);
		prop.setWaterColor(0x725113);
		
		river = new BiomeGenRiver(prop);
		river.setBiomeId(GenesisConfig.riverId);
		BiomeDictionary.registerBiomeType(river, BiomeDictionary.Type.RIVER, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Shallow Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-0.7F);
		prop.setHeightVariation(0.0F);
		prop.setWaterColor(0x9F791F);
		
		shallowOcean = new BiomeGenShallowOcean(prop);
		shallowOcean.setBiomeId(GenesisConfig.shallowOceanId);
		BiomeDictionary.registerBiomeType(shallowOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		prop = new BiomeGenBase.BiomeProperties("Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-1.0F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x9F791F);
		
		ocean = new BiomeGenOceanGenesis(prop).addElements(1);
		ocean.setBiomeId(GenesisConfig.oceanId);
		BiomeDictionary.registerBiomeType(ocean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		prop = new BiomeGenBase.BiomeProperties("Deep Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-1.8F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x876719);
		
		deepOcean = new BiomeGenOceanGenesis(prop).addElements(0);
		deepOcean.setBiomeId(GenesisConfig.deepOceanId);
		BiomeDictionary.registerBiomeType(deepOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		prop = new BiomeGenBase.BiomeProperties("Beach");
		prop.setTemperature(1.15F);
		prop.setRainfall(1.0F);
		prop.setBaseHeight(0.0F);
		prop.setHeightVariation(0.025F);
		prop.setWaterColor(0x725113);
		
		genesisBeach = new BiomeGenBeachGenesis(prop);
		genesisBeach.setBiomeId(GenesisConfig.genesisBeachId);
		BiomeDictionary.registerBiomeType(genesisBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Red Beach");
		prop.setTemperature(2.0F);
		prop.setRainfall(0.0F);
		prop.setRainDisabled();
		prop.setBaseHeight(0.0F);
		prop.setHeightVariation(0.025F);
		prop.setWaterColor(0x725113);
		
		redBeach = new BiomeGenRedBeach(prop);
		redBeach.setBiomeId(GenesisConfig.redBeachId);
		BiomeDictionary.registerBiomeType(redBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Limestone Beach");
		prop.setTemperature(0.8F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(0.05F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x725113);
		
		limestoneBeach = new BiomeGenLimestoneBeach(prop);
		limestoneBeach.setBiomeId(GenesisConfig.limestoneBeachId);
		BiomeDictionary.registerBiomeType(limestoneBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
	}
}
