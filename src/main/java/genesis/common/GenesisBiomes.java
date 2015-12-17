package genesis.common;

import genesis.world.biome.BiomeGenMetaForest;
import genesis.world.biome.BiomeGenMetaForestM;
import genesis.world.biome.BiomeGenRedBeach;
import genesis.world.biome.BiomeGenRedDesert;
import genesis.world.biome.BiomeGenRedDesertM;
import genesis.world.biome.BiomeGenFloodplainsForest;
import genesis.world.biome.BiomeGenAuxForest;
import genesis.world.biome.BiomeGenAuxForestM;
import genesis.world.biome.BiomeGenAuxPlains;
import genesis.world.biome.BiomeGenBaseGenesis;
import genesis.world.biome.BiomeGenBeachGenesis;
import genesis.world.biome.BiomeGenLimestoneBeach;
import genesis.world.biome.BiomeGenMarsh;
import genesis.world.biome.BiomeGenOceanGenesis;
import genesis.world.biome.BiomeGenRainforest;
import genesis.world.biome.BiomeGenRainforestEdge;
import genesis.world.biome.BiomeGenRainforestEdgeM;
import genesis.world.biome.BiomeGenRainforestM;
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
	public static BiomeGenBaseGenesis rainforestEdge;
	public static BiomeGenBaseGenesis rainforestEdgeM;
	public static BiomeGenBaseGenesis rainforestHills;
	public static BiomeGenBaseGenesis auxForest;
	public static BiomeGenBaseGenesis auxForestM;
	public static BiomeGenBaseGenesis auxForestHills;
	public static BiomeGenBaseGenesis auxPlains;
	public static BiomeGenBaseGenesis metaForest;
	public static BiomeGenBaseGenesis metaForestM;
	public static BiomeGenBaseGenesis metaForestHills;
	public static BiomeGenBaseGenesis swampRainForest;
	public static BiomeGenBaseGenesis marsh;
	public static BiomeGenBaseGenesis floodplainsForest;
	public static BiomeGenBaseGenesis redDesert;
	public static BiomeGenBaseGenesis redDesertHills;
	public static BiomeGenBaseGenesis redDesertM;
	public static BiomeGenBaseGenesis river;
	public static BiomeGenBaseGenesis shallowOcean;
	public static BiomeGenBaseGenesis ocean;
	public static BiomeGenBaseGenesis deepOcean;
	public static BiomeGenBaseGenesis genesisBeach;
	public static BiomeGenBaseGenesis redBeach;
	public static BiomeGenBaseGenesis limestoneBeach;
	
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
		
		rainforestEdge = new BiomeGenRainforestEdge(GenesisConfig.rainforestEdgeId);
		BiomeDictionary.registerBiomeType(rainforestEdge, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		rainforestEdgeM = new BiomeGenRainforestEdgeM(GenesisConfig.rainforestEdgeId+128);
		BiomeDictionary.registerBiomeType(rainforestEdgeM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		
		rainforestHills = new BiomeGenRainforest(GenesisConfig.rainforestHillsId).setBiomeName("Rainforest Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(rainforestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		auxForest = new BiomeGenAuxForest(GenesisConfig.auxForestId);
		BiomeManagerGenesis.registerBiome(auxForest, BiomeType.WARM, GenesisConfig.auxForestWeight);
		BiomeDictionary.registerBiomeType(auxForest, BiomeDictionary.Type.FOREST);
		
		auxForestM = new BiomeGenAuxForestM(GenesisConfig.auxForestId+128);
		BiomeDictionary.registerBiomeType(auxForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN);
		
		auxForestHills = new BiomeGenAuxForest(GenesisConfig.auxForestHillsId).setBiomeName("Araucarioxylon Forest Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST);
		
		auxPlains = new BiomeGenAuxPlains(GenesisConfig.auxPlainsId);
		BiomeManagerGenesis.registerBiome(auxPlains, BiomeType.WARM, GenesisConfig.auxPlainsWeight);
		BiomeDictionary.registerBiomeType(auxPlains, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SPARSE);
		
		metaForest = new BiomeGenMetaForest(GenesisConfig.metaForestId);
		BiomeManagerGenesis.registerBiome(metaForest, BiomeType.WARM, GenesisConfig.metaForestWeight);
		BiomeDictionary.registerBiomeType(metaForest, BiomeDictionary.Type.FOREST);
		
		metaForestM = new BiomeGenMetaForestM(GenesisConfig.metaForestId+128);
		BiomeDictionary.registerBiomeType(metaForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN);
		
		metaForestHills = new BiomeGenMetaForest(GenesisConfig.metaForestHillsId).setBiomeName("Metasequoia Forest Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(auxForestHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST);
		
		swampRainForest = new BiomeGenSwampRainforest(GenesisConfig.swampRainForestId);
		BiomeDictionary.registerBiomeType(swampRainForest, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		marsh = new BiomeGenMarsh(GenesisConfig.marshId);
		BiomeManagerGenesis.registerBiome(marsh, BiomeType.WARM, GenesisConfig.marshWeight);
		BiomeDictionary.registerBiomeType(marsh, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		floodplainsForest = new BiomeGenFloodplainsForest(GenesisConfig.floodplainsForestId);
		BiomeManagerGenesis.registerBiome(floodplainsForest, BiomeType.WARM, GenesisConfig.floodplainsForestWeight);
		BiomeDictionary.registerBiomeType(floodplainsForest, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		redDesert = new BiomeGenRedDesert(GenesisConfig.redDesertId);
		BiomeManagerGenesis.registerBiome(redDesert, BiomeType.DESERT, GenesisConfig.redDesertWeight);
		BiomeDictionary.registerBiomeType(redDesert, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		
		redDesertHills = new BiomeGenRedDesert(GenesisConfig.redDesertHillsId).setBiomeName("Red Desert Hills").setHeight(height_LowHills);
		BiomeDictionary.registerBiomeType(redDesertHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		
		redDesertM = new BiomeGenRedDesertM(GenesisConfig.redDesertId+128);
		BiomeDictionary.registerBiomeType(redDesertM, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.DRY);
		
		river = new BiomeGenRiver(GenesisConfig.riverId).setHeight(height_ShallowWaters);
		BiomeDictionary.registerBiomeType(river, BiomeDictionary.Type.RIVER, BiomeDictionary.Type.WET);
		
		shallowOcean = new BiomeGenShallowOcean(GenesisConfig.shallowOceanId);
		BiomeDictionary.registerBiomeType(shallowOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		ocean = new BiomeGenOceanGenesis(GenesisConfig.oceanId).addElements(1).setBiomeName("Ocean").setHeight(height_Oceans);
		BiomeDictionary.registerBiomeType(ocean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		deepOcean = new BiomeGenOceanGenesis(GenesisConfig.deepOceanId).addElements(0).setWaterColor(0x01723c).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
		BiomeDictionary.registerBiomeType(deepOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		genesisBeach = new BiomeGenBeachGenesis(GenesisConfig.genesisBeachId).setHeight(height_Shore);
		BiomeDictionary.registerBiomeType(genesisBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		redBeach = new BiomeGenRedBeach(GenesisConfig.redBeachId).setHeight(height_Shore);
		BiomeDictionary.registerBiomeType(redBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
		
		limestoneBeach = new BiomeGenLimestoneBeach(GenesisConfig.limestoneBeachId);
		BiomeDictionary.registerBiomeType(limestoneBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
	}
}
