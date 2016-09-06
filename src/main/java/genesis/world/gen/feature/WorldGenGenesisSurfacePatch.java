package genesis.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
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

	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		do
		{
			IBlockState state = world.getBlockState(pos);

			if (state.getMaterial() == Material.WATER)
				return false;

			if (!state.getBlock().isAir(state, world, pos)
					&& !state.getBlock().isLeaves(state, world, pos))

				break;

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
							IBlockState state = world.getBlockState(pos.add(x, y, z));
							Block blockToReplace = world.getBiome(pos.add(x, y, z)).topBlock.getBlock();
							/*
							if (rand.nextInt(3) == 0)
							{
								if (currentState == filler)
									currentState = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, rand.nextInt(3));
								else
									currentState = filler;
							}
							*/
							if (state.getBlock() == blockToReplace
									&& world.getBlockState(pos.add(x, y, z).up()).getMaterial() != Material.WATER)
								world.setBlockState(pos.add(x, y, z), currentState, 2);
						}
					}
				}
			}
		}

		return true;
	}
}
