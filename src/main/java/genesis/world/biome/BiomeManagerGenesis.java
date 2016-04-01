package genesis.world.biome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

import com.google.common.collect.Maps;

public class BiomeManagerGenesis
{
	private static final Map<BiomeType, List<BiomeEntry>> biomes = Maps.newHashMap();

	public static boolean registerBiome(BiomeGenBaseGenesis biome, int id, BiomeType type, int weight)
	{
		BiomeGenBase.registerBiome(id, biome.getBiomeName(), biome);
		biome.setBiomeId(id);
		
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
			biomes.put(type, new ArrayList<BiomeEntry>());
		}
		
		return biomes.get(type);
	}

}
