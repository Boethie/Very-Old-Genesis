package genesis.common;

import genesis.world.biome.BiomeGenAuxForest;
import genesis.world.biome.BiomeGenAuxForestM;
import genesis.world.biome.BiomeGenBaseGenesis;
import genesis.world.biome.BiomeGenBeachGenesis;
import genesis.world.biome.BiomeGenDeepOceanGenesis;
import genesis.world.biome.BiomeGenFloodplainsForest;
import genesis.world.biome.BiomeGenLimestoneBeach;
import genesis.world.biome.BiomeGenMarsh;
import genesis.world.biome.BiomeGenMetaForest;
import genesis.world.biome.BiomeGenMetaForestM;
import genesis.world.biome.BiomeGenOceanGenesis;
import genesis.world.biome.BiomeGenRainforest;
import genesis.world.biome.BiomeGenRainforestM;
import genesis.world.biome.BiomeGenRedBeach;
import genesis.world.biome.BiomeGenRedDesert;
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
	public static BiomeGenBaseGenesis river;
	public static BiomeGenBaseGenesis shallowOcean;
	public static BiomeGenBaseGenesis ocean;
	public static BiomeGenBaseGenesis deepOcean;
	public static BiomeGenBaseGenesis genesisBeach;
	public static BiomeGenBaseGenesis redBeach;
	public static BiomeGenBaseGenesis limestoneBeach;
	public static BiomeGenBaseGenesis redDesert;
	
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
		BiomeGenBase.registerBiome(GenesisConfig.rainforestId + 128, rainforestM.getBiomeName(), rainforestM);
		BiomeDictionary.registerBiomeType(rainforestM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		
		prop = new BiomeGenBase.BiomeProperties("Rainforest Hills");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		rainforestHills = new BiomeGenRainforest(prop);
		BiomeGenBase.registerBiome(GenesisConfig.rainforestHillsId, rainforestHills.getBiomeName(), rainforestHills);
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
		BiomeGenBase.registerBiome(GenesisConfig.auxForestId + 128, auxForestM.getBiomeName(), auxForestM);
		BiomeDictionary.registerBiomeType(auxForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Araucarioxylon Forest Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		auxForestHills = new BiomeGenAuxForest(prop);
		BiomeGenBase.registerBiome(GenesisConfig.auxForestHillsId, auxForestHills.getBiomeName(), auxForestHills);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Woodlands");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.035F);
		prop.setHeightVariation(0.1F);
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
		BiomeGenBase.registerBiome(GenesisConfig.woodlandsId + 128, woodlandsM.getBiomeName(), woodlandsM);
		BiomeDictionary.registerBiomeType(woodlandsM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Woodlands Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		woodlandsHills = new BiomeGenWoodlands(prop);
		BiomeGenBase.registerBiome(GenesisConfig.woodlandsHillsId, woodlandsHills.getBiomeName(), woodlandsHills);
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
		BiomeGenBase.registerBiome(GenesisConfig.metaForestId + 128, metaForestM.getBiomeName(), metaForestM);
		BiomeDictionary.registerBiomeType(metaForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Metasequoia Forest Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		metaForestHills = new BiomeGenMetaForest(prop);
		BiomeGenBase.registerBiome(GenesisConfig.metaForestHillsId, metaForestHills.getBiomeName(), metaForestHills);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Swamp Rainforest");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(-0.2F);
		prop.setHeightVariation(0.03F);
		prop.setWaterColor(0x725113);
		
		swampRainForest = new BiomeGenSwampRainforest(prop);
		BiomeGenBase.registerBiome(GenesisConfig.swampRainForestId, swampRainForest.getBiomeName(), swampRainForest);
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
		BiomeManagerGenesis.registerBiome(floodplainsForest, GenesisConfig.floodplainsForestId, BiomeType.WARM, GenesisConfig.floodplainsForestWeight);
		BiomeDictionary.registerBiomeType(floodplainsForest, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		prop = new BiomeGenBase.BiomeProperties("River");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-0.5F);
		prop.setHeightVariation(0.0F);
		prop.setWaterColor(0x725113);
		
		river = new BiomeGenRiver(prop);
		BiomeGenBase.registerBiome(GenesisConfig.riverId, river.getBiomeName(), river);
		BiomeDictionary.registerBiomeType(river, BiomeDictionary.Type.RIVER, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Shallow Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-0.7F);
		prop.setHeightVariation(0.0F);
		prop.setWaterColor(0x9F791F);
		
		shallowOcean = new BiomeGenShallowOcean(prop);
		BiomeGenBase.registerBiome(GenesisConfig.shallowOceanId, shallowOcean.getBiomeName(), shallowOcean);
		BiomeDictionary.registerBiomeType(shallowOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		prop = new BiomeGenBase.BiomeProperties("Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-1.0F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x9F791F);
		
		ocean = new BiomeGenOceanGenesis(prop).addElements(1);
		BiomeGenBase.registerBiome(GenesisConfig.oceanId, ocean.getBiomeName(), ocean);
		BiomeDictionary.registerBiomeType(ocean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		prop = new BiomeGenBase.BiomeProperties("Deep Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-1.8F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x876719);
		
		deepOcean = new BiomeGenDeepOceanGenesis(prop);
		BiomeGenBase.registerBiome(GenesisConfig.deepOceanId, deepOcean.getBiomeName(), deepOcean);
		BiomeDictionary.registerBiomeType(deepOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		prop = new BiomeGenBase.BiomeProperties("Beach");
		prop.setTemperature(1.15F);
		prop.setRainfall(1.0F);
		prop.setBaseHeight(0.0F);
		prop.setHeightVariation(0.025F);
		prop.setWaterColor(0x725113);
		
		genesisBeach = new BiomeGenBeachGenesis(prop);
		BiomeGenBase.registerBiome(GenesisConfig.genesisBeachId, genesisBeach.getBiomeName(), genesisBeach);
		BiomeDictionary.registerBiomeType(genesisBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Red Beach");
		prop.setTemperature(2.0F);
		prop.setRainfall(0.0F);
		prop.setRainDisabled();
		prop.setBaseHeight(0.0F);
		prop.setHeightVariation(0.025F);
		prop.setWaterColor(0x725113);
		
		redBeach = new BiomeGenRedBeach(prop);
		BiomeGenBase.registerBiome(GenesisConfig.redBeachId, redBeach.getBiomeName(), redBeach);
		BiomeDictionary.registerBiomeType(redBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Limestone Beach");
		prop.setTemperature(0.8F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(0.05F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x725113);
		
		limestoneBeach = new BiomeGenLimestoneBeach(prop);
		BiomeGenBase.registerBiome(GenesisConfig.limestoneBeachId, limestoneBeach.getBiomeName(), limestoneBeach);
		BiomeDictionary.registerBiomeType(limestoneBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		prop = new BiomeGenBase.BiomeProperties("Red Desert");
		prop.setTemperature(2.0F);
		prop.setRainfall(0.0F);
		prop.setRainDisabled();
		prop.setBaseHeight(0.15F);
		prop.setHeightVariation(0.35F);
		prop.setWaterColor(0x725113);
		
		redDesert = new BiomeGenRedDesert(prop);
		BiomeManagerGenesis.registerBiome(redDesert, GenesisConfig.redDesertId, BiomeType.DESERT, GenesisConfig.redDesertWeight);
		BiomeDictionary.registerBiomeType(redDesert, BiomeDictionary.Type.DRY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY);
	}
}
