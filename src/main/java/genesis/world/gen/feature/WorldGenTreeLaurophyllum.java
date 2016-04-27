package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.i.IntRange;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenTreeLaurophyllum extends WorldGenTreeBase
{
	public WorldGenTreeLaurophyllum(int minHeight, int maxHeight, boolean notify)
	{
		super(GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.SAPLING, EnumTree.LAUROPHYLLUM),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.BRANCH, EnumTree.LAUROPHYLLUM),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.LAUROPHYLLUM),
				null,
				IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		if (!isCubeClear(world, pos.up(), 1, height))
			return false;
		
		for (int y = 0; y < height; y++)
		{
			BlockPos branchPos = pos.up(y);
			
			setBlockInWorld(world, branchPos, wood);
			
			if (y > 0 && rand.nextInt(6) == 0)
			{
				for (int i = 0; i < 4; i++)
					if (rand.nextInt(3) == 0)
						setBlockInWorld(world, branchPos.add(rand.nextInt(3) - 1, 0, 0), wood);
				
				for (int i = 0; i < 4; i++)
					if (rand.nextInt(3) == 0)
						setBlockInWorld(world, branchPos.add(0, 0, rand.nextInt(3) - 1), wood);
			}
			
			super.doBranchLeaves(world, branchPos, rand, true, MathHelper.clamp_int(y + 1, 0, 2), true);
		}
		
		return true;
	}
}
