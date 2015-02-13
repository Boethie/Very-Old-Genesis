package genesis.world;

import genesis.common.GenesisBlocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class GenesisWorldHelper 
{
	public static int getTopBlockOfType(World world, int x, int z, Block... blocks) 
	{
		ArrayList<Block> blockArray = new ArrayList<Block>();
		Collections.addAll(blockArray, blocks);
		Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
		int k = chunk.getTopFilledSegment() + 15;
		x &= 15;

		for (z &= 15; k > 0; --k) 
		{
			Block block = chunk.getBlock(x, k, z);

			if (blockArray.contains(block)) 
			{
				return k + 1;
			}
		}

		return -1;
	}

	public static int getTopBlockNotOfType(World world, int x, int z, Block... blocks) 
	{
		ArrayList<Block> blockArray = new ArrayList<Block>();
		Collections.addAll(blockArray, blocks);
		Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
		int k = chunk.getTopFilledSegment() + 15;
		x &= 15;

		for (z &= 15; k > 0; --k) 
		{
			Block block = chunk.getBlock(x, k, z);

			if (!blockArray.contains(block) && block != Blocks.air)
			{
				return k + 1;
			}
		}

		return -1;
	}

	public static int getTopBlockAfterType(World world, int x, int z, Block... blocks) 
	{
		boolean hasReachedType = false;
		ArrayList<Block> blockArray = new ArrayList<Block>();
		Collections.addAll(blockArray, blocks);
		Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
		int k = chunk.getTopFilledSegment() + 15;
		x &= 15;

		for (z &= 15; k > 0; --k) 
		{
			Block block = chunk.getBlock(x, k, z);

			if (blockArray.contains(block)) hasReachedType = true;
			if (!blockArray.contains(block) && block != Blocks.air && hasReachedType) 
			{
				return k + 1;
			}
		}

		return -1;
	}

	public static int getBlockHeight(World world, int x, int z) 
	{
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
		return chunk.getHeight(x & 15, z & 15);
	}

	public static void generateMossyGraniteBoulder(World world, Random rand, BlockPos pos) 
	{
		for (int a = 0; a < 4; a++) 
		{
			int posX = pos.getX() + rand.nextInt(16);
			int posZ = pos.getZ() + rand.nextInt(16);
			int posY = GenesisWorldHelper.getTopBlockOfType(world, posX, posZ, GenesisBlocks.moss, Blocks.dirt);
			BlockPos thisPos = new BlockPos(posX, posY, posZ);
			if (world.isAirBlock(thisPos)) 
			{
				int width = rand.nextInt(2);
				int length = rand.nextInt(2);
				int height = rand.nextInt(3);

				generateEllipsoid(GenesisBlocks.mossy_granite.getDefaultState(), world, rand, thisPos, width, length, height);
			}
		}
	}

	public static void generateEllipsoid(IBlockState state, World world, Random rand, BlockPos pos, int width, int length, int height) 
	{
		for (int y = (int) Math.ceil(pos.getY() - height); y <= (int) Math.ceil(pos.getY() + height); y++) 
		{
			for (int x = (int) Math.ceil(pos.getX() - width); x <= (int) Math.ceil(pos.getX() + width); x++) 
			{
				for (int z = (int) Math.ceil(pos.getZ() - length); z <= (int) Math.ceil(pos.getZ() + length); z++) 
				{
					double xfr = z - pos.getZ();
					double zfr = x - pos.getX();
					double yfr = y - pos.getY();

					//Equation of ellipsoid
					if ( (xfr*xfr)/(width*width) + (zfr*zfr)/(length*length) + (yfr*yfr)/(height*height) <= 1.5) 
					{
						BlockPos thisPos = new BlockPos(x, y, z);

						if(world.isAirBlock(thisPos) && rand.nextInt(15) > 0) 
						{
							world.setBlockState(thisPos, state, 2);
						}
					}
				}
			}
		}
	}
}
