package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeLaurophyllum extends WorldGenTreeBase
{
	public WorldGenTreeLaurophyllum(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.BRANCH, EnumTree.LAUROPHYLLUM),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.LAUROPHYLLUM),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		pos = getTreePos(world, pos);
		
		if (!canTreeGrow(world, pos))
			return false;
		
		if (rand.nextInt(rarity) != 0)
			return false;
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight);
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
			
			if (i > 0 && i < treeHeight - 1)
			{
				if (rand.nextInt(3) == 0)
					setBlockInWorld(world, pos.up(i).add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1), wood);
				if (rand.nextInt(3) == 0)
					setBlockInWorld(world, pos.up(i).add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1), wood);
				if (rand.nextInt(3) == 0)
					setBlockInWorld(world, pos.up(i).add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1), wood);
				if (rand.nextInt(3) == 0)
					setBlockInWorld(world, pos.up(i).add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1), wood);
				
				super.doBranchLeaves(world, pos.up(i), rand, true, i, true);
			}
		}
		
		return true;
	}
}
