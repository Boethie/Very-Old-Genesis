package genesis.common;

import genesis.world.biome.BiomeGenArchaeopterisForest;
import genesis.world.biome.BiomeGenAuxForest;
import genesis.world.biome.BiomeGenAuxForestEdge;
import genesis.world.biome.BiomeGenAuxForestEdgeM;
import genesis.world.biome.BiomeGenAuxForestM;
import genesis.world.biome.BiomeGenBaseGenesis;
import genesis.world.biome.BiomeGenLimestoneBeach;
import genesis.world.biome.BiomeGenRainforest;
import genesis.world.biome.BiomeGenRainforestEdge;
import genesis.world.biome.BiomeGenRainforestEdgeM;
import genesis.world.biome.BiomeGenRainforestM;
import genesis.world.biome.BiomeGenRiver;
import genesis.world.biome.BiomeGenShallowOcean;
import genesis.world.biome.BiomeGenSwampRainforest;
import genesis.world.biome.BiomeManagerGenesis;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;

public final class GenesisBiomes
{
	
	public static BiomeGenBaseGenesis rainforest;
	public static BiomeGenBaseGenesis rainforestM;
	public static BiomeGenBaseGenesis rainforestEdge;
	public static BiomeGenBaseGenesis rainforestEdgeM;
	public static BiomeGenBaseGenesis auxForest;
	public static BiomeGenBaseGenesis auxForestM;
	public static BiomeGenBaseGenesis auxForestEdge;
	public static BiomeGenBaseGenesis auxForestEdgeM;
	public static BiomeGenBaseGenesis river;
	public static BiomeGenBaseGenesis shallowOcean;
	public static BiomeGenBaseGenesis limestoneBeach;
	public static BiomeGenBaseGenesis swampRainForest;
	public static BiomeGenBaseGenesis archaeopterisForest;
	
	public static void loadBiomes()
	{
		rainforest = new BiomeGenRainforest(GenesisConfig.rainforestId).setColor(5470985);
		BiomeManagerGenesis.registerBiome(rainforest, BiomeType.WARM, GenesisConfig.rainforestWeight);
		BiomeDictionary.registerBiomeType(rainforest, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		rainforestM = new BiomeGenRainforestM(GenesisConfig.rainforestId+128).setColor(5470985);
		BiomeDictionary.registerBiomeType(rainforestM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		
		rainforestEdge = new BiomeGenRainforestEdge(GenesisConfig.rainforestEdgeId).setColor(6458135);
		BiomeDictionary.registerBiomeType(rainforestEdge, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		rainforestEdgeM = new BiomeGenRainforestEdgeM(GenesisConfig.rainforestEdgeId+128).setColor(6458135);
		BiomeDictionary.registerBiomeType(rainforestEdgeM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		
		auxForest = new BiomeGenAuxForest(GenesisConfig.auxForestId);
		BiomeManagerGenesis.registerBiome(auxForest, BiomeType.WARM, GenesisConfig.auxForestWeight);
		BiomeDictionary.registerBiomeType(auxForest, BiomeDictionary.Type.FOREST);
		
		auxForestM = new BiomeGenAuxForestM(GenesisConfig.auxForestId+128);
		BiomeDictionary.registerBiomeType(auxForestM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN);
		
		auxForestEdge = new BiomeGenAuxForestEdge(GenesisConfig.auxForestEdgeId);
		BiomeDictionary.registerBiomeType(auxForestEdge, BiomeDictionary.Type.FOREST);
		
		auxForestEdgeM = new BiomeGenAuxForestEdgeM(GenesisConfig.auxForestEdgeId+128);
		BiomeDictionary.registerBiomeType(auxForestEdgeM, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN);
		
		swampRainForest = new BiomeGenSwampRainforest(GenesisConfig.swampRainForestId);
		BiomeManagerGenesis.registerBiome(swampRainForest, BiomeType.WARM, GenesisConfig.swampRainForestWeight);
		BiomeDictionary.registerBiomeType(swampRainForest, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		archaeopterisForest = new BiomeGenArchaeopterisForest(GenesisConfig.archaeopterisForestId);
		BiomeManagerGenesis.registerBiome(archaeopterisForest, BiomeType.WARM, GenesisConfig.archaeopterisForestWeight);
		BiomeDictionary.registerBiomeType(archaeopterisForest, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		river = new BiomeGenRiver(GenesisConfig.riverId);
		BiomeDictionary.registerBiomeType(river, BiomeDictionary.Type.RIVER, BiomeDictionary.Type.WET);
		
		shallowOcean = new BiomeGenShallowOcean(GenesisConfig.shallowOceanId);
		BiomeDictionary.registerBiomeType(shallowOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		limestoneBeach = new BiomeGenLimestoneBeach(GenesisConfig.limestoneBeachId);
		BiomeDictionary.registerBiomeType(limestoneBeach, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET);
	}
}
