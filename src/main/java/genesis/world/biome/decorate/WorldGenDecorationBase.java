package genesis.world.biome.decorate;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenDecorationBase extends WorldGenerator
{
	private int countPerChunk = 0;
	
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		return false;
	}
	
	public WorldGenDecorationBase setCountPerChunk(int count)
	{
		countPerChunk = count;
		return this;
	}
	
	public int getCountPerChunk()
	{
		return countPerChunk;
	}
}
