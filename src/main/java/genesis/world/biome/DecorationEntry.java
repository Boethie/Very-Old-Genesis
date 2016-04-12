package genesis.world.biome;

import java.util.Random;

import genesis.util.random.i.RandomIntProvider;
import net.minecraft.world.gen.feature.WorldGenerator;

public class DecorationEntry
{
	private final WorldGenerator generator;
	private final RandomIntProvider chunkCount;
	
	public DecorationEntry(WorldGenerator generator, RandomIntProvider chunkCount)
	{
		this.generator = generator;
		this.chunkCount = chunkCount;
	}
	
	public WorldGenerator getGenerator()
	{
		return generator;
	}
	
	public int getCountPerChunk(Random rand)
	{
		return chunkCount.get(rand);
	}
}
