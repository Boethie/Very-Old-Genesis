package genesis.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenTreeLepidodendron extends WorldGenTreeBase 
{
	/**
	 * Constructor
	 *
	 * @param minH   minimum height of tree trunk
	 * @param maxH   max possible height above minH the tree trunk could grow
	 * @param notify whether or not to notify blocks of the tree being grown.
	 *               Generally false for world generation, true for saplings.
	 */
	public WorldGenTreeLepidodendron(int minH, int maxH, boolean notify) 
	{
		//super(new BlockAndMeta(GenesisTreeBlocks.logs[TreeType.LEPIDODENDRON.ordinal()], 0), new BlockAndMeta(GenesisTreeBlocks.leaves[TreeType.LEPIDODENDRON.ordinal()], 0), notify);
		//TODO: Change this to the right log!
		super(Blocks.log.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), Blocks.leaves.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), notify);

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
		if (!isCubeClear(pos.add(0, 2, 0), 1, 15)) 
		{
			return false;
		}

		// generates the trunk
		pos = pos.add(0, 1, 0);
		int treeHeight = minHeight + random.nextInt(maxHeight);

		// Generate trunk
		for (int i = 0; i < treeHeight; i++) 
		{
			setBlockInWorld(pos.add(0, i, 0), wood);
		}
		// Generate leaves
		BlockPos topPos = pos.add(0, treeHeight - 2, 0);
		generateLeafLayerCircle(world, random, 1, topPos);
		generateLeafLayerCircle(world, random, 3.5, topPos.add(0, 1, 0));
		generateLeafLayerCircle(world, random, 2.5, topPos.add(0, 2, 0));
		generateLeafLayerCircle(world, random, 1.5, topPos.add(0, 3, 0));
		setBlockInWorld(topPos.add(0, 4, 0), leaves);

		return true;
	}
}
