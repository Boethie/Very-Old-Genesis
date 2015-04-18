package genesis.common;

import genesis.world.biome.BiomeGenRainforest;

public final class GenesisBiomes
{
	
	public static BiomeGenRainforest rainforest;
	
	public static void loadBiomes()
	{
		rainforest = new BiomeGenRainforest(GenesisConfig.rainforestId);
	}
}
