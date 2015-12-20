package genesis.world.biome.decorate;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenDecorationBase extends WorldGenerator
{
	private int countPerChunk = 0;
	private int patchSize = 0;
	
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
	
	public WorldGenDecorationBase setPatchSize(int size)
	{
		patchSize = size;
		return this;
	}
	
	public int getPatchSize()
	{
		return (patchSize == 0)? 1 : patchSize;
	}
	
	public BlockPos getPosition(World world, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isAir(world, pos) && !block.isLeaves(world, pos))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		return pos;
	}
	
	protected void setBlockInWorld(World world, BlockPos pos, IBlockState state)
	{
		setBlockInWorld(world, pos, state, false);
	}
	
	protected void setBlockInWorld(World world, BlockPos pos, IBlockState state, boolean force)
	{
		boolean place = true;
		
		try
		{
			if (!(world.getBlockState(pos).getBlock().isAir(world, pos)) && !force)
			{
				place = false;
			}
			
			if (place)
			{
				world.setBlockState(pos, state, 3);
			}
		}
		catch(Exception e)
		{
		}
	}
	
	public boolean findBlockInRange(World world, BlockPos pos, Block findWhat, int distanceX, int distanceY, int distanceZ)
	{
		boolean blockExists = false;
		
		found:
		for (int x = -distanceX; x <= distanceX; ++x)
		{
			for (int z = -distanceZ; z <= distanceZ; ++z)
			{
				for (int y = -distanceY; y <= distanceY; ++y)
				{
					if (world.getBlockState(pos.add(x, y, z)).getBlock() == findWhat)
					{
						blockExists = true;
						break found;
					}
				}
			}
		}
		
		return blockExists;
	}
	
	public boolean findBlockInRange(World world, BlockPos pos, IBlockState findWhat, int distanceX, int distanceY, int distanceZ)
	{
		boolean blockExists = false;
		
		found:
		for (int x = -distanceX; x <= distanceX; ++x)
		{
			for (int z = -distanceZ; z <= distanceZ; ++z)
			{
				for (int y = -distanceY; y <= distanceY; ++y)
				{
					if (world.getBlockState(pos.add(x, y, z)) == findWhat)
					{
						blockExists = true;
						break found;
					}
				}
			}
		}
		
		return blockExists;
	}
	
	public IBlockState getSpawnablePlant(Random rand)
	{
		return null;
	}
}
