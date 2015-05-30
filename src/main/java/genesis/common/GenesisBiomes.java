package genesis.common;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeType;
import genesis.world.biome.BiomeGenAuxForest;
import genesis.world.biome.BiomeGenAuxForestEdge;
import genesis.world.biome.BiomeGenRainforest;
import genesis.world.biome.BiomeGenRainforestEdge;
import genesis.world.biome.BiomeGenRainforestEdgeM;
import genesis.world.biome.BiomeGenRainforestM;
import genesis.world.biome.BiomeGenRiver;
import genesis.world.biome.BiomeGenShallowOcean;
import genesis.world.biome.BiomeManagerGenesis;

public final class GenesisBiomes
{
	
	public static BiomeGenRainforest rainforest;
	public static BiomeGenRainforestM rainforestM;
	public static BiomeGenRainforestEdge rainforestEdge;
	public static BiomeGenRainforestEdgeM rainforestEdgeM;
	public static BiomeGenAuxForest auxForest;
	public static BiomeGenAuxForestEdge auxForestEdge;
	public static BiomeGenRiver river;
	public static BiomeGenShallowOcean shallowOcean;
	
	public static void loadBiomes()
	{
		rainforest = new BiomeGenRainforest(GenesisConfig.rainforestId);
		BiomeManagerGenesis.registerBiome(rainforest, BiomeType.WARM, GenesisConfig.rainforestWeight);
		BiomeDictionary.registerBiomeType(rainforest, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		rainforestM = new BiomeGenRainforestM(GenesisConfig.rainforestId+128);
		BiomeDictionary.registerBiomeType(rainforestM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		
		rainforestEdge = new BiomeGenRainforestEdge(GenesisConfig.rainforestEdgeId);
		BiomeDictionary.registerBiomeType(rainforestEdge, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET);
		
		rainforestEdgeM = new BiomeGenRainforestEdgeM(GenesisConfig.rainforestEdgeId+128);
		BiomeDictionary.registerBiomeType(rainforestEdgeM, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.MOUNTAIN);
		
		river = new BiomeGenRiver(GenesisConfig.riverId);
		BiomeDictionary.registerBiomeType(river, BiomeDictionary.Type.RIVER, BiomeDictionary.Type.WET);
		
		shallowOcean = new BiomeGenShallowOcean(GenesisConfig.shallowOceanId);
		BiomeDictionary.registerBiomeType(shallowOcean, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.WET, BiomeDictionary.Type.WATER);
		
		auxForest = new BiomeGenAuxForest(GenesisConfig.auxForestId);
		BiomeManagerGenesis.registerBiome(auxForest, BiomeType.WARM, GenesisConfig.auxForestWeight);
		BiomeDictionary.registerBiomeType(auxForest, BiomeDictionary.Type.FOREST);
		auxForestEdge = new BiomeGenAuxForestEdge(GenesisConfig.auxForestEdgeId);
		BiomeDictionary.registerBiomeType(auxForestEdge, BiomeDictionary.Type.FOREST);
	}
}
