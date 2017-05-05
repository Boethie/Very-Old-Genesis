package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.BlockVolumeShape;
import genesis.util.random.i.IntRange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeTropidogyne extends WorldGenTreeBase
{
	public WorldGenTreeTropidogyne(int minHeight, int maxHeight, boolean notify)
	{
		super(GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.SAPLING, EnumTree.TROPIDOGYNE),
				GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.BRANCH, EnumTree.TROPIDOGYNE),
				GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.TROPIDOGYNE),
				null,
				IntRange.create(minHeight, maxHeight), notify);
	}
	
	public static WorldGenTreeTropidogyne makeDefaultWithNotify(boolean notify)
	{
		return new WorldGenTreeTropidogyne(3, 6, notify);
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		int base = rand.nextInt(3);
		
		if (!BlockVolumeShape.region(-1, 1, -1, 1, height, 1)
				.hasSpace(pos, isEmptySpace(world)))
			return false;
		
		for (int i = 0; i < height; i++)
		{
			BlockPos currPos = pos.add(0, i, 0);
			setBlockInWorld(world, currPos, this.wood);
			if (i >= base)
			{
				leaves(world, rand, height, i, currPos);
				if (i > 0 && rand.nextBoolean())
				{
					boolean xCoord = rand.nextBoolean();
					int coord = rand.nextBoolean()? -1: 1;
					int dx = xCoord? coord: 0;
					int dz = !xCoord? coord: 0;
					BlockPos branch = currPos.add(dx, 0, dz);
					setBlockInWorld(world, branch, this.wood);
					leaves(world, rand, height, i, branch);
				}
			}
		}
		return true;
	}
	
	private void leaves(World world, Random rand, int height, int y, BlockPos currPos)
	{
		int leaveSize;
		boolean cap = false;
		// make the leaves size 1 at the top, and add cap
		if (y == height - 1)
		{
			leaveSize = 1;
			cap = true;
		}
		else
		{
			// otherwise random
			leaveSize = rand.nextInt(2) + 1;
		}
		doBranchLeaves(world, currPos, rand, cap, leaveSize, true);
	}
}
