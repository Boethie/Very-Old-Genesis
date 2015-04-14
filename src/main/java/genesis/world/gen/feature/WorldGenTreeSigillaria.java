package genesis.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeSigillaria extends WorldGenTreeBase 
{

	/**
	 * Constructor
	 *
	 * @param minH   minimum height of tree trunk
	 * @param maxH   max possible height above minH the tree trunk could grow
	 * @param notify whether or not to notify blocks of the tree being grown.
	 *               Generally false for world generation, true for saplings.
	 */
	public WorldGenTreeSigillaria(int minH, int maxH, boolean notify) 
	{
		//super(new BlockAndMeta(GenesisTreeBlocks.logs[TreeType.SIGILLARIA.ordinal()], 0), new BlockAndMeta(GenesisTreeBlocks.leaves[TreeType.SIGILLARIA.ordinal()], 0), notify);
		//TODO: Change this to the right log!
		super(Blocks.log2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), Blocks.leaves2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), notify);
		minHeight = minH;
		maxHeight = maxH;
	}

	@Override
	public boolean generate(World world, Random random, BlockPos generatePos) 
	{
		BlockPos pos = generatePos.add(0,0,0);
		this.world = world;
		this.random = random;

		// finds top block for the given x,z position (excluding leaves and
		// grass)
		for (boolean var6 = false; world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isLeaves(world, pos) && pos.getY() > 0; pos = pos.add(0, -1, 0)) 
		{
			;
		}
		// locY is now the highest solid terrain block

		//Check that the tree can grow here
		Block soil = world.getBlockState(pos).getBlock();
		if(!this.canTreeGrow(soil)) 
		{
			return false;
		}

		//Check that there is enough room
		if (!isCubeClear(pos.add(0, 2, 0), 3, 15)) 
		{
			return false;
		}

		// generates the trunk
		pos = pos.add(0, 1, 0);
		int treeHeight = minHeight + random.nextInt(maxHeight);

		type = random.nextInt(2);
		// tree type 1: single trunk
		if (type == 0) 
		{
			for (int i = 0; i < treeHeight; i++) 
			{
				setBlockInWorld(pos.add(0, i, 0), wood);
			}
			BlockPos topPos = pos.add(0, treeHeight - 3, 0);
			generateLeafLayerCircle(world, random, 1, topPos);
			generateLeafLayerCircle(world, random, 1, topPos.add(0, 1, 0));
			generateLeafLayerCircle(world, random, 1, topPos.add(0, 2, 0));
			generateLeafLayerCircle(world, random, 1, topPos.add(0, 3, 0));
			setBlockInWorld(topPos.add(0, 4, 0), leaves);
		}

		// tree type 2: split trunk
		else if (type == 1) 
		{
			for (int i = 0; i < treeHeight - 5; i++) 
			{
				setBlockInWorld(pos.add(0, i, 0), wood);
			}
			int currentHeight = treeHeight - 5;
			int radius = 1;

			// trunks along x
			if (random.nextInt(2) == 0) 
			{
				setBlockInWorld(pos.add(radius, currentHeight, 0), wood);
				setBlockInWorld(pos.add(-radius, currentHeight, 0), wood);
				radius++;
				currentHeight++;
				setBlockInWorld(pos.add(radius, currentHeight, 0), wood);
				setBlockInWorld(pos.add(-radius, currentHeight, 0), wood);
				currentHeight++;
				setBlockInWorld(pos.add(radius, currentHeight, 0), wood);
				setBlockInWorld(pos.add(-radius, currentHeight, 0), wood);

				for (int a = 0; a < 2; a++) 
				{
					int randomHeight = random.nextInt(2);
					setBlockInWorld(pos.add(radius, currentHeight + randomHeight, 0), wood);
					BlockPos topPos = pos.add(0, treeHeight - 3, 0);
					generateLeafLayerCircle(world, random, 1, topPos.add(radius, randomHeight, 0));
					generateLeafLayerCircle(world, random, 1, topPos.add(radius, randomHeight+1, 0));
					generateLeafLayerCircle(world, random, 1, topPos.add(radius, randomHeight+2, 0));
					generateLeafLayerCircle(world, random, 1, topPos.add(radius, randomHeight+3, 0));
					setBlockInWorld(topPos.add(radius, randomHeight+4, 0), leaves);
					radius *= -1;
				}
			}
			// trunks along y
			else 
			{
				setBlockInWorld(pos.add(0, currentHeight, radius), wood);
				setBlockInWorld(pos.add(0, currentHeight, -radius), wood);
				radius++;
				currentHeight++;
				setBlockInWorld(pos.add(0, currentHeight, radius), wood);
				setBlockInWorld(pos.add(0, currentHeight, -radius), wood);
				currentHeight++;
				setBlockInWorld(pos.add(0, currentHeight, radius), wood);
				setBlockInWorld(pos.add(0, currentHeight, -radius), wood);

				for (int a = 0; a < 2; a++) 
				{
					int randomHeight = random.nextInt(2);
					setBlockInWorld(pos.add(0, currentHeight + randomHeight, radius), wood);
					BlockPos topPos = pos.add(0, treeHeight - 3, 0);
					generateLeafLayerCircle(world, random, 1, topPos.add(0, randomHeight, radius));
					generateLeafLayerCircle(world, random, 1, topPos.add(0, randomHeight+1, radius));
					generateLeafLayerCircle(world, random, 1, topPos.add(0, randomHeight+2, radius));
					generateLeafLayerCircle(world, random, 1, topPos.add(0, randomHeight+3, radius));
					setBlockInWorld(topPos.add(0, randomHeight+4, radius), leaves);
					radius *= -1;
				}
			}
		}
		return true;
	}
}
