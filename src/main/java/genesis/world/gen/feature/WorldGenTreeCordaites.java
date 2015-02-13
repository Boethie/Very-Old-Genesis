package genesis.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeCordaites extends WorldGenTreeBase 
{
	/**
	 * Constructor
	 *
	 * @param minH   minimum height of tree trunk
	 * @param maxH   max possible height above minH the tree trunk could grow
	 * @param notify whether or not to notify blocks of the tree being grown.
	 *               Generally false for world generation, true for saplings.
	 */
	public WorldGenTreeCordaites(int minH, int maxH, boolean notify) 
	{
		//super(new BlockAndMeta(GenesisTreeBlocks.logs[TreeType.CORDAITES.ordinal()], 0), new BlockAndMeta(GenesisTreeBlocks.leaves[TreeType.CORDAITES.ordinal()], 0), notify);
		//TODO: Change this to the right log!
		super(Blocks.log.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), Blocks.leaves.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), notify);
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
		for (int i = 2; i < treeHeight; i++) 
		{
			setBlockInWorld(pos.add(0, i, 0), wood);
		}

		// generate roots
		generateCordiatesRoots(world, random, pos.add(0, 2, 0));

		// generates leaves at top
		int currentHeight;

		for (currentHeight = treeHeight - 3; currentHeight < treeHeight; currentHeight++) 
		{
			generateLeafLayerCircleNoise(world, random, 3, pos.add(random.nextInt(2), currentHeight, random.nextInt(2)));
		}

		generateLeafLayerCircleNoise(world, random, 2.8, pos.add(random.nextInt(2), currentHeight, random.nextInt(2)));
		currentHeight++;
		generateLeafLayerCircleNoise(world, random, 1.5, pos.add(random.nextInt(2), currentHeight, random.nextInt(2)));
		currentHeight++;
		generateLeafLayerCircleNoise(world, random, 1, pos.add(random.nextInt(2), currentHeight, random.nextInt(2)));

		// generates branches with leaves
		currentHeight -= 8;
		int firstDir = random.nextInt(4);

		for (int i = 0; i < 4; i++) 
		{
			BlockPos xyz = generateStraightBranch(world, random, 3, pos.add(0, currentHeight + i, 0), (firstDir + i) % 4);
			generateLeafLayerCircleNoise(world, random, 1.5, xyz.add(0, -1, 0));
			generateLeafLayerCircleNoise(world, random, 2, xyz);
			generateLeafLayerCircleNoise(world, random, 1.5, xyz.add(0, 1, 0));
		}
		return true;
	}

	// Tree helper methods:

	protected void generateCordiatesRoots(World world, Random random, BlockPos pos) 
	{
		int[] i = {1, 0, -1, -1, -1, 0, 1, 1};
		int[] k = {1, 1, 1, 0, -1, -1, -1, 0};

		for (int a = 0; a < 8; a++) 
		{
			int length = random.nextInt(3);

			for (int b = 0; b < length; b++) 
			{
				setBlockInWorld(pos.add(i[a] * b, 0, k[a] * b), wood);
			}

			for (int b = 0; b < 4; b++) 
			{
				setBlockInWorld(pos.add(i[a] * (b + length), -b, k[a] * (b + length)), wood);
			}
		}
	}
}
