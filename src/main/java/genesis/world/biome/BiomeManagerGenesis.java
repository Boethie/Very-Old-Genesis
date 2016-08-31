package genesis.world.biome;

import java.util.*;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class BiomeManagerGenesis
{
	private static final Map<BiomeType, List<BiomeEntry>> biomes = new HashMap<>();
	
	public static boolean registerBiome(BiomeGenesis biome, int id, BiomeType type, int weight)
	{
		Biome.registerBiome(id, biome.getBiomeName(), biome);
		
		return getEntries(type).add(new BiomeEntry(biome, weight));
	}
	
	public static Map<BiomeType, List<BiomeEntry>> getEntriesMap()
	{
		return Collections.unmodifiableMap(biomes);
	}
	
	public static List<BiomeEntry> getEntries(BiomeType type)
	{
		if (!biomes.containsKey(type))
		{
			biomes.put(type, new ArrayList<>());
		}
		
		return biomes.get(type);
	}
}
