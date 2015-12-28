package genesis.world.biome.gen.feature;

import java.util.Random;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.SiltBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenGenesisSurfacePatch extends WorldGenerator
{
	protected static final int SIZE_X = 15;
	protected static final int SIZE_Y = 7;
	protected static final int SIZE_Z = 15;
	
	protected IBlockState filler;
	
	public WorldGenGenesisSurfacePatch(IBlockState filler)
	{
		this.filler = filler;
	}
	
	protected static int index(int x, int y, int z)
	{
		return (x << 7) | (z << 3) | y;
	}
	
	protected static boolean is(boolean[] bools, int x, int y, int z)
	{
		return bools[index(x, y, z)];
	}
	
	protected static boolean neighborIs(boolean[] bools, int x, int y, int z)
	{
		if (x < SIZE_X && bools[index(x + 1, y, z)])
			return true;
		if (x > 0 && bools[index(x - 1, y, z)])
			return true;
		if (y < SIZE_Y && bools[index(x, y + 1, z)])
			return true;
		if (y > 0 && bools[index(x, y - 1, z)])
			return true;
		if (z < SIZE_Z && bools[index(x, y, z + 1)])
			return true;
		if (z > 0 && bools[index(x, y, z - 1)])
			return true;
		
		return false;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		Block block;
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (
					!block.isAir(world, pos) 
					&& !block.isLeaves(world, pos)
					&& !(block == Blocks.water)
			)
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		boolean[] isLake = new boolean[2048];
		int circles = 5 + rand.nextInt(10);
		
		// Calculate whether each position in the area should be part of a lake.
		for (int i = 0; i < circles; i++)
		{
			double sizeX = rand.nextDouble() * 6 + 3;
			double sizeY = rand.nextDouble() * 4 + 2;
			double sizeZ = rand.nextDouble() * 6 + 3;
			double offX = rand.nextDouble() * (16 - sizeX - 2) + 1 + sizeX / 2;
			double offY = rand.nextDouble() * (8 - sizeY - 4) + 2 + sizeY / 2;
			double offZ = rand.nextDouble() * (16 - sizeZ - 2) + 1 + sizeZ / 2;
			
			for (int x = 1; x < SIZE_X; x++)
			{
				for (int y = 1; y < SIZE_Y; y++)
				{
					for (int z = 1; z < SIZE_Z; z++)
					{
						double dX = (x - offX) / (sizeX / 2);
						double dY = (y - offY) / (sizeY / 2);
						double dZ = (z - offZ) / (sizeZ / 2);
						double dist = dX * dX + dY * dY + dZ * dZ;
						
						if (dist < 1)
							isLake[index(x, y, z)] = true;
					}
				}
			}
		}
		
		int x, y, z;
		
		IBlockState currentState = filler;
		
		for (x = 0; x <= SIZE_X; ++x)
		{
			for (y = 0; y < SIZE_Y; ++y)
			{
				for (z = 0; z < SIZE_Z; ++z)
				{
					if (is(isLake, x, y, z))
					{
						if (world.isBlockLoaded(pos.add(x, y, z)))
						{
							block = world.getBlockState(pos.add(x, y, z)).getBlock();
							
							if (rand.nextInt(3) == 0)
							{
								if (currentState == filler)
									currentState = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, rand.nextInt(3));
								else
									currentState = filler;
							}
							
							if (block.getDefaultState() == GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT).getDefaultState())
								world.setBlockState(pos.add(x, y, z), currentState, 2);
						}
					}
				}
			}
		}
		
		return true;
	}
}
