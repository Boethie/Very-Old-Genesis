package genesis.common;

import genesis.combo.SiltBlocks;
import genesis.combo.variant.EnumSilt;
import genesis.world.biome.BiomeAuxForest;
import genesis.world.biome.BiomeAuxForestM;
import genesis.world.biome.BiomeGenesis;
import genesis.world.biome.BiomeBeachGenesis;
import genesis.world.biome.BiomeDeepOceanGenesis;
import genesis.world.biome.BiomeFloodplainsForest;
import genesis.world.biome.BiomeLimestoneBeach;
import genesis.world.biome.BiomeMarsh;
import genesis.world.biome.BiomeMetaForest;
import genesis.world.biome.BiomeMetaForestM;
import genesis.world.biome.BiomeOceanGenesis;
import genesis.world.biome.BiomeRainforest;
import genesis.world.biome.BiomeRainforestIslands;
import genesis.world.biome.BiomeRainforestM;
import genesis.world.biome.BiomeRedBeach;
import genesis.world.biome.BiomeRedDesert;
import genesis.world.biome.BiomeRiver;
import genesis.world.biome.BiomeShallowOcean;
import genesis.world.biome.BiomeSwampRainforest;
import genesis.world.biome.BiomeWoodlands;
import genesis.world.biome.BiomeWoodlandsM;
import genesis.world.biome.BiomeManagerGenesis;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeType;

public final class GenesisBiomes
{
	public static BiomeGenesis rainforest;
	public static BiomeGenesis rainforestM;
	public static BiomeGenesis rainforestHills;
	public static BiomeGenesis rainforestIslands;
	public static BiomeGenesis auxForest;
	public static BiomeGenesis auxForestM;
	public static BiomeGenesis auxForestHills;
	public static BiomeGenesis metaForest;
	public static BiomeGenesis metaForestM;
	public static BiomeGenesis metaForestHills;
	public static BiomeGenesis woodlands;
	public static BiomeGenesis woodlandsM;
	public static BiomeGenesis woodlandsHills;
	public static BiomeGenesis swampRainForest;
	public static BiomeGenesis marsh;
	public static BiomeGenesis floodplainsForest;
	public static BiomeGenesis river;
	public static BiomeGenesis shallowOcean;
	public static BiomeGenesis ocean;
	public static BiomeGenesis deepOcean;
	public static BiomeGenesis genesisBeach;
	public static BiomeGenesis redBeach;
	public static BiomeGenesis limestoneBeach;
	public static BiomeGenesis redDesert;
	public static BiomeGenesis redDesertHills;
	
	public static void loadBiomes()
	{
		Biome.BiomeProperties prop;
		
		prop = new Biome.BiomeProperties("Rainforest");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(0.35F);
		prop.setHeightVariation(0.2F);
		prop.setWaterColor(0x725113);
		
		rainforest = new BiomeRainforest(prop);
		BiomeManagerGenesis.registerBiome(rainforest, GenesisConfig.rainforestId, BiomeType.WARM, GenesisConfig.rainforestWeight);
		BiomeDictionary.registerBiomeType(rainforest, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new Biome.BiomeProperties("Rainforest M");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(0.275F);
		prop.setHeightVariation(0.35F);
		prop.setWaterColor(0x725113);
		
		rainforestM = new BiomeRainforestM(prop);
		Biome.registerBiome(GenesisConfig.rainforestId + 128, rainforestM.getBiomeName(), rainforestM);
		BiomeDictionary.registerBiomeType(rainforestM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		
		prop = new Biome.BiomeProperties("Rainforest Hills");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		rainforestHills = new BiomeRainforest(prop);
		Biome.registerBiome(GenesisConfig.rainforestHillsId, rainforestHills.getBiomeName(), rainforestHills);
		BiomeDictionary.registerBiomeType(rainforestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new Biome.BiomeProperties("Rainforest Islands");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(-0.5F);
		prop.setHeightVariation(0.425F);
		prop.setWaterColor(0x9F791F);
		
		rainforestIslands = new BiomeRainforestIslands(prop);
		Biome.registerBiome(GenesisConfig.rainforestId + 129, rainforestIslands.getBiomeName(), rainforestIslands);
		BiomeDictionary.registerBiomeType(rainforestIslands, BiomeDictionary.Type.OCEAN);
		
		prop = new Biome.BiomeProperties("Araucarioxylon Forest");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.1F);
		prop.setHeightVariation(0.175F);
		prop.setWaterColor(0x725113);
		
		auxForest = new BiomeAuxForest(prop);
		BiomeManagerGenesis.registerBiome(auxForest, GenesisConfig.auxForestId, BiomeType.WARM, GenesisConfig.auxForestWeight);
		BiomeDictionary.registerBiomeType(auxForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT);
		
		prop = new Biome.BiomeProperties("Araucarioxylon Forest M");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.275F);
		prop.setHeightVariation(0.35F);
		prop.setWaterColor(0x725113);
		
		auxForestM = new BiomeAuxForestM(prop);
		Biome.registerBiome(GenesisConfig.auxForestId + 128, auxForestM.getBiomeName(), auxForestM);
		BiomeDictionary.registerBiomeType(auxForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT);
		
		prop = new Biome.BiomeProperties("Araucarioxylon Forest Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		auxForestHills = new BiomeAuxForest(prop);
		Biome.registerBiome(GenesisConfig.auxForestHillsId, auxForestHills.getBiomeName(), auxForestHills);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT);
		
		prop = new Biome.BiomeProperties("Woodlands");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.035F);
		prop.setHeightVariation(0.15F);
		prop.setWaterColor(0x725113);
		
		woodlands = new BiomeWoodlands(prop);
		BiomeManagerGenesis.registerBiome(woodlands, GenesisConfig.woodlandsId, BiomeType.WARM, GenesisConfig.woodlandsWeight);
		BiomeDictionary.registerBiomeType(woodlands, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new Biome.BiomeProperties("Woodlands M");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.525F);
		prop.setHeightVariation(0.5F);
		prop.setWaterColor(0x725113);
		
		woodlandsM = new BiomeWoodlandsM(prop);
		Biome.registerBiome(GenesisConfig.woodlandsId + 128, woodlandsM.getBiomeName(), woodlandsM);
		BiomeDictionary.registerBiomeType(woodlandsM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new Biome.BiomeProperties("Woodlands Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		woodlandsHills = new BiomeWoodlands(prop);
		Biome.registerBiome(GenesisConfig.woodlandsHillsId, woodlandsHills.getBiomeName(), woodlandsHills);
		BiomeDictionary.registerBiomeType(woodlandsHills, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new Biome.BiomeProperties("Metasequoia Forest");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.075F);
		prop.setHeightVariation(0.175F);
		prop.setWaterColor(0x725113);
		
		metaForest = new BiomeMetaForest(prop);
		BiomeManagerGenesis.registerBiome(metaForest, GenesisConfig.metaForestId, BiomeType.WARM, GenesisConfig.metaForestWeight);
		BiomeDictionary.registerBiomeType(metaForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT);
		
		prop = new Biome.BiomeProperties("Metasequoia Forest M");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.275F);
		prop.setHeightVariation(0.35F);
		prop.setWaterColor(0x725113);
		
		metaForestM = new BiomeMetaForestM(prop);
		Biome.registerBiome(GenesisConfig.metaForestId + 128, metaForestM.getBiomeName(), metaForestM);
		BiomeDictionary.registerBiomeType(metaForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT);
		
		prop = new Biome.BiomeProperties("Metasequoia Forest Hills");
		prop.setTemperature(1.1F);
		prop.setRainfall(0.9F);
		prop.setBaseHeight(0.425F);
		prop.setHeightVariation(0.275F);
		prop.setWaterColor(0x725113);
		
		metaForestHills = new BiomeMetaForest(prop);
		Biome.registerBiome(GenesisConfig.metaForestHillsId, metaForestHills.getBiomeName(), metaForestHills);
		BiomeDictionary.registerBiomeType(metaForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT);
		
		prop = new Biome.BiomeProperties("Swamp Rainforest");
		prop.setTemperature(0.95F);
		prop.setRainfall(1.4F);
		prop.setBaseHeight(-0.2F);
		prop.setHeightVariation(0.03F);
		prop.setWaterColor(0x725113);
		
		swampRainForest = new BiomeSwampRainforest(prop);
		Biome.registerBiome(GenesisConfig.swampRainForestId, swampRainForest.getBiomeName(), swampRainForest);
		BiomeDictionary.registerBiomeType(swampRainForest, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new Biome.BiomeProperties("Marsh");
		prop.setTemperature(1.15F);
		prop.setRainfall(0.3F);
		prop.setBaseHeight(0.0F);
		prop.setHeightVariation(-0.01F);
		prop.setWaterColor(0x725113);
		
		marsh = new BiomeMarsh(prop);
		BiomeManagerGenesis.registerBiome(marsh, GenesisConfig.marshId, BiomeType.WARM, GenesisConfig.marshWeight);
		BiomeDictionary.registerBiomeType(marsh, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new Biome.BiomeProperties("Floodplains Forest");
		prop.setTemperature(1.15F);
		prop.setRainfall(1.0F);
		prop.setBaseHeight(-0.2F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x725113);
		
		floodplainsForest = new BiomeFloodplainsForest(prop);
		BiomeManagerGenesis.registerBiome(floodplainsForest, GenesisConfig.floodplainsForestId, BiomeType.WARM, GenesisConfig.floodplainsForestWeight);
		BiomeDictionary.registerBiomeType(floodplainsForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		prop = new Biome.BiomeProperties("River");
		prop.setTemperature(0.6F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-0.55F);
		prop.setHeightVariation(0.0F);
		prop.setWaterColor(0x725113);
		
		river = new BiomeRiver(prop);
		Biome.registerBiome(GenesisConfig.riverId, river.getBiomeName(), river);
		BiomeDictionary.registerBiomeType(river, BiomeDictionary.Type.RIVER);
		
		prop = new Biome.BiomeProperties("Shallow Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-0.7F);
		prop.setHeightVariation(0.0F);
		prop.setWaterColor(0x9F791F);
		
		shallowOcean = new BiomeShallowOcean(prop);
		Biome.registerBiome(GenesisConfig.shallowOceanId, shallowOcean.getBiomeName(), shallowOcean);
		BiomeDictionary.registerBiomeType(shallowOcean, BiomeDictionary.Type.OCEAN);
		
		prop = new Biome.BiomeProperties("Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-1.0F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x9F791F);
		
		ocean = new BiomeOceanGenesis(prop);
		Biome.registerBiome(GenesisConfig.oceanId, ocean.getBiomeName(), ocean);
		BiomeDictionary.registerBiomeType(ocean, BiomeDictionary.Type.OCEAN);
		
		prop = new Biome.BiomeProperties("Deep Ocean");
		prop.setTemperature(0.5F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(-1.8F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x876719);
		
		deepOcean = new BiomeDeepOceanGenesis(prop);
		Biome.registerBiome(GenesisConfig.deepOceanId, deepOcean.getBiomeName(), deepOcean);
		BiomeDictionary.registerBiomeType(deepOcean, BiomeDictionary.Type.OCEAN);
		
		prop = new Biome.BiomeProperties("Beach");
		prop.setTemperature(0.8F);
		prop.setRainfall(1.0F);
		prop.setBaseHeight(0.0F);
		prop.setHeightVariation(0.025F);
		prop.setWaterColor(0x725113);
		
		genesisBeach = new BiomeBeachGenesis(prop);
		Biome.registerBiome(GenesisConfig.genesisBeachId, genesisBeach.getBiomeName(), genesisBeach);
		BiomeDictionary.registerBiomeType(genesisBeach, BiomeDictionary.Type.BEACH);
		
		prop = new Biome.BiomeProperties("Red Beach");
		prop.setTemperature(2.0F);
		prop.setRainfall(0.0F);
		prop.setRainDisabled();
		prop.setBaseHeight(0.0F);
		prop.setHeightVariation(0.025F);
		prop.setWaterColor(0x725113);
		
		redBeach = new BiomeRedBeach(prop);
		Biome.registerBiome(GenesisConfig.redBeachId, redBeach.getBiomeName(), redBeach);
		BiomeDictionary.registerBiomeType(redBeach, BiomeDictionary.Type.BEACH);
		
		prop = new Biome.BiomeProperties("Limestone Beach");
		prop.setTemperature(0.8F);
		prop.setRainfall(0.4F);
		prop.setBaseHeight(0.05F);
		prop.setHeightVariation(0.1F);
		prop.setWaterColor(0x725113);
		
		limestoneBeach = new BiomeLimestoneBeach(prop);
		Biome.registerBiome(GenesisConfig.limestoneBeachId, limestoneBeach.getBiomeName(), limestoneBeach);
		BiomeDictionary.registerBiomeType(limestoneBeach, BiomeDictionary.Type.BEACH);
		
		prop = new Biome.BiomeProperties("Red Desert");
		prop.setTemperature(2.0F);
		prop.setRainfall(0.0F);
		prop.setRainDisabled();
		prop.setBaseHeight(0.15F);
		prop.setHeightVariation(0.05F);
		prop.setWaterColor(0x725113);
		
		redDesert = new BiomeRedDesert(prop).addTrees();
		BiomeManagerGenesis.registerBiome(redDesert, GenesisConfig.redDesertId, BiomeType.DESERT, GenesisConfig.redDesertWeight);
		BiomeDictionary.registerBiomeType(redDesert, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY);
		
		prop = new Biome.BiomeProperties("Red Desert Hills");
		prop.setTemperature(2.0F);
		prop.setRainfall(0.0F);
		prop.setRainDisabled();
		prop.setBaseHeight(0.825F);
		prop.setHeightVariation(0.02F);
		prop.setWaterColor(0x725113);
		
		redDesertHills = new BiomeRedDesert(prop).setTopBlock(GenesisBlocks.silt.getBlockState(SiltBlocks.SILTSTONE, EnumSilt.RED_SILT)).setIsHills(true).addTrees();
		Biome.registerBiome(GenesisConfig.redDesertId + 128, redDesertHills.getBiomeName(), redDesertHills);
		BiomeDictionary.registerBiomeType(redDesertHills, BiomeDictionary.Type.DRY, BiomeDictionary.Type.MESA, BiomeDictionary.Type.SANDY);
	}
}
