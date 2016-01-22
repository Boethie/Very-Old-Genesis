package genesis.common;

import genesis.world.biome.BiomeGenAuxForest;
import genesis.world.biome.BiomeGenAuxForestM;
import genesis.world.biome.BiomeGenBaseGenesis;
import genesis.world.biome.BiomeGenBeachGenesis;
import genesis.world.biome.BiomeGenDryophyllumForest;
import genesis.world.biome.BiomeGenDryophyllumForestM;
import genesis.world.biome.BiomeGenFloodplainsForest;
import genesis.world.biome.BiomeGenLimestoneBeach;
import genesis.world.biome.BiomeGenMarsh;
import genesis.world.biome.BiomeGenMetaForest;
import genesis.world.biome.BiomeGenMetaForestM;
import genesis.world.biome.BiomeGenOceanGenesis;
import genesis.world.biome.BiomeGenRainforest;
import genesis.world.biome.BiomeGenRainforestM;
import genesis.world.biome.BiomeGenRedBeach;
import genesis.world.biome.BiomeGenRedLowlands;
import genesis.world.biome.BiomeGenRedLowlandsM;
import genesis.world.biome.BiomeGenRiver;
import genesis.world.biome.BiomeGenShallowOcean;
import genesis.world.biome.BiomeGenSwampRainforest;
import genesis.world.biome.BiomeManagerGenesis;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeType;

public final class GenesisBiomes
{
	public static BiomeGenBaseGenesis rainforest;
	public static BiomeGenBaseGenesis rainforestM;
	//public static BiomeGenBaseGenesis rainforestEdge;
	//public static BiomeGenBaseGenesis rainforestEdgeM;
	public static BiomeGenBaseGenesis rainforestHills;
	public static BiomeGenBaseGenesis auxForest;
	public static BiomeGenBaseGenesis auxForestM;
	public static BiomeGenBaseGenesis auxForestHills;
	public static BiomeGenBaseGenesis metaForest;
	public static BiomeGenBaseGenesis metaForestM;
	public static BiomeGenBaseGenesis metaForestHills;
	public static BiomeGenBaseGenesis dryophyllumForest;
	public static BiomeGenBaseGenesis dryophyllumForestM;
	public static BiomeGenBaseGenesis dryophyllumForestHills;
	public static BiomeGenBaseGenesis swampRainForest;
	public static BiomeGenBaseGenesis marsh;
	public static BiomeGenBaseGenesis floodplainsForest;
	public static BiomeGenBaseGenesis redLowlands;
	public static BiomeGenBaseGenesis redLowlandsHills;
	public static BiomeGenBaseGenesis redLowlandsM;
	public static BiomeGenBaseGenesis river;
	public static BiomeGenBaseGenesis shallowOcean;
	public static BiomeGenBaseGenesis ocean;
	public static BiomeGenBaseGenesis deepOcean;
	public static BiomeGenBaseGenesis genesisBeach;
	public static BiomeGenBaseGenesis redBeach;
	public static BiomeGenBaseGenesis limestoneBeach;
	
	/*
	public static BiomeGenBaseGenesis savanna;
	public static BiomeGenBaseGenesis savannaM;
	*/
	
	public static final BiomeGenBase.Height height_ShallowWaters = new BiomeGenBase.Height(-0.5F, 0.0F);
	public static final BiomeGenBase.Height height_Oceans = new BiomeGenBase.Height(-1.0F, 0.1F);
	public static final BiomeGenBase.Height height_DeepOceans = new BiomeGenBase.Height(-1.8F, 0.1F);
	public static final BiomeGenBase.Height height_EmergingHills = new BiomeGenBase.Height(0.0F, 0.1F);
	public static final BiomeGenBase.Height height_LowHills = new BiomeGenBase.Height(0.45F, 0.3F);
	public static final BiomeGenBase.Height height_Shore = new BiomeGenBase.Height(0.0F, 0.025F);
	
	public static void loadBiomes()
	{
		rainforest = new BiomeGenRainforest(GenesisConfig.rainforestId);
		BiomeManagerGenesis.registerBiome(rainforest, BiomeType.WARM, GenesisConfig.rainforestWeight);
		BiomeDictionary.registerBiomeType(rainforest, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		rainforestM = new BiomeGenRainforestM(GenesisConfig.rainforestId+128);
		BiomeDictionary.registerBiomeType(rainforestM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		/*
		rainforestEdge = new BiomeGenRainforestEdge(GenesisConfig.rainforestEdgeId);
		BiomeDictionary.registerBiomeType(rainforestEdge, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		rainforestEdgeM = new BiomeGenRainforestEdgeM(GenesisConfig.rainforestEdgeId+128);
		BiomeDictionary.registerBiomeType(rainforestEdgeM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		*/
		rainforestHills = new BiomeGenRainforest(GenesisConfig.rainforestHillsId).setBiomeName("Rainforest Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(rainforestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		auxForest = new BiomeGenAuxForest(GenesisConfig.auxForestId);
		BiomeManagerGenesis.registerBiome(auxForest, BiomeType.WARM, GenesisConfig.auxForestWeight);
		BiomeDictionary.registerBiomeType(auxForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		auxForestM = new BiomeGenAuxForestM(GenesisConfig.auxForestId+128);
		BiomeDictionary.registerBiomeType(auxForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		auxForestHills = new BiomeGenAuxForest(GenesisConfig.auxForestHillsId).setBiomeName("Araucarioxylon Forest Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		dryophyllumForest = new BiomeGenDryophyllumForest(GenesisConfig.dryophyllumForestId).setBiomeName("Dryophyllum Forest");
		BiomeManagerGenesis.registerBiome(dryophyllumForest, BiomeType.WARM, GenesisConfig.dryophyllumForestWeight);
		BiomeDictionary.registerBiomeType(dryophyllumForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		dryophyllumForestM = new BiomeGenDryophyllumForestM(GenesisConfig.dryophyllumForestId+128).setBiomeName("Dryophyllum Forest M");
		BiomeDictionary.registerBiomeType(dryophyllumForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		dryophyllumForestHills = new BiomeGenDryophyllumForest(GenesisConfig.dryophyllumForestHillsId).setBiomeName("Dryophyllum Forest Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(dryophyllumForestHills, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		/*
		savanna = new BiomeGenSavanna(GenesisConfig.savannaId);
		BiomeManagerGenesis.registerBiome(savanna, BiomeType.WARM, GenesisConfig.savannaWeight);
		BiomeDictionary.registerBiomeType(savanna, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
		
		savannaM = new BiomeGenSavannaM(GenesisConfig.savannaId+128);
		BiomeDictionary.registerBiomeType(savannaM, BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY);
		*/
		metaForest = new BiomeGenMetaForest(GenesisConfig.metaForestId);
		BiomeManagerGenesis.registerBiome(metaForest, BiomeType.WARM, GenesisConfig.metaForestWeight);
		BiomeDictionary.registerBiomeType(metaForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		metaForestM = new BiomeGenMetaForestM(GenesisConfig.metaForestId+128);
		BiomeDictionary.registerBiomeType(metaForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		metaForestHills = new BiomeGenMetaForest(GenesisConfig.metaForestHillsId).setBiomeName("Metasequoia Forest Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		swampRainForest = new BiomeGenSwampRainforest(GenesisConfig.swampRainForestId);
		BiomeDictionary.registerBiomeType(swampRainForest, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		marsh = new BiomeGenMarsh(GenesisConfig.marshId);
		BiomeManagerGenesis.registerBiome(marsh, BiomeType.WARM, GenesisConfig.marshWeight);
		BiomeDictionary.registerBiomeType(marsh, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		floodplainsForest = new BiomeGenFloodplainsForest(GenesisConfig.floodplainsForestId);
		BiomeManagerGenesis.registerBiome(floodplainsForest, BiomeType.WARM, GenesisConfig.floodplainsForestWeight);
		BiomeDictionary.registerBiomeType(floodplainsForest, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		redLowlands = new BiomeGenRedLowlands(GenesisConfig.redLowlandsId);
		BiomeManagerGenesis.registerBiome(redLowlands, BiomeType.WARM, GenesisConfig.redLowlandsWeight);
		BiomeDictionary.registerBiomeType(redLowlands, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		
		redLowlandsHills = new BiomeGenRedLowlands(GenesisConfig.redLowlandsHillsId).setBiomeName("Red Lowlands Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(redLowlandsHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		
		redLowlandsM = new BiomeGenRedLowlandsM(GenesisConfig.redLowlandsId+128);
		BiomeDictionary.registerBiomeType(redLowlandsM, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		
		river = new BiomeGenRiver(GenesisConfig.riverId).setHeight(height_ShallowWaters);
		BiomeDictionary.registerBiomeType(river, BiomeDictionary.Type.RIVER, BiomeDictionary.Type.WET);
		
		shallowOcean = new BiomeGenShallowOcean(GenesisConfig.shallowOceanId);
		BiomeDictionary.registerBiomeType(shallowOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		ocean = new BiomeGenOceanGenesis(GenesisConfig.oceanId).addElements(1).setBiomeName("Ocean").setHeight(height_Oceans);
		BiomeDictionary.registerBiomeType(ocean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		deepOcean = new BiomeGenOceanGenesis(GenesisConfig.deepOceanId).addElements(0).setWaterColor(0x876719).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
		BiomeDictionary.registerBiomeType(deepOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		genesisBeach = new BiomeGenBeachGenesis(GenesisConfig.genesisBeachId).setHeight(height_Shore);
		BiomeDictionary.registerBiomeType(genesisBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		redBeach = new BiomeGenRedBeach(GenesisConfig.redBeachId).setHeight(height_Shore);
		BiomeDictionary.registerBiomeType(redBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		limestoneBeach = new BiomeGenLimestoneBeach(GenesisConfig.limestoneBeachId);
		BiomeDictionary.registerBiomeType(limestoneBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
	}
}
