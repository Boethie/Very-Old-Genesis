package genesis.world;

import genesis.common.Genesis;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class GenesisWorldGenerator implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimensionId()) 
		{
			case -1:
				generateNether(random, chunkX * 16, chunkZ * 16, world, chunkGenerator, chunkProvider);
				break;
			case 0:
				generateSurface(random, chunkX * 16, chunkZ * 16, world, chunkGenerator, chunkProvider);
				break;
			case 1:
				generateEnd(random, chunkX * 16, chunkZ * 16, world, chunkGenerator, chunkProvider);
				break;
			default:
				if(world.provider.getDimensionId() == Genesis.dimensionId) 
				{
					generateGenesis(random, chunkX * 16, chunkZ * 16, world, chunkGenerator, chunkProvider);
				}
		}
	}

	private void generateNether(Random random, int i, int j, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{

	}

	private void generateSurface(Random random, int i, int j, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{

	}

	private void generateEnd(Random random, int i, int j, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{

	}

	private void generateGenesis(Random random, int i, int j, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{

	}
}
