package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeGinkgo extends WorldGenTreeBase
{
	public WorldGenTreeGinkgo(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.GINKGO).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.GINKGO),
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
		int base = 2 + rand.nextInt(4);
		
		if (!isCubeClear(world, pos.up(base), 4, treeHeight))
		{
			return false;
		}
		
		int mainBranches = 3 + rand.nextInt(6);
		
		for (int i = 0; i < mainBranches; ++i)
		{
			base = 2 + rand.nextInt(4);
			branchUp(world, pos, rand, treeHeight, base);
		}
		
		return true;
	}
	
	private void branchUp(World world, BlockPos pos, Random rand, int height, int base)
	{
		int fallX = 1 - rand.nextInt(3);
		int fallZ = 1 - rand.nextInt(3);
		int fallCount = 0;
		BlockPos upPos = pos.down();
		EnumAxis woodAxis = EnumAxis.Y;
		
		for (int i = 0; i < height; i++)
		{
			if (rand.nextInt(3) == 0 && i > base && fallCount < 3)
			{
				fallCount++;
				
				upPos = upPos.add(fallX, 0, fallZ);
				
				if (fallX != 0)
					woodAxis = EnumAxis.X;
				else if (fallZ != 0)
					woodAxis = EnumAxis.Z;
				else
					woodAxis = EnumAxis.Y;
				
				if (rand.nextInt(3) == 0 || (fallX == 0 && fallZ == 0))
					upPos = upPos.up();
			}
			else
			{
				fallCount = 0;
				
				woodAxis = EnumAxis.Y;
				upPos = upPos.up();
			}
			
			setBlockInWorld(world, upPos, wood.withProperty(BlockLog.LOG_AXIS, woodAxis));
			
			if (i == base)
				doBranchLeaves(world, upPos, rand, false, 2, true);
			
			if (i > base)
			{
				if (i > base + 2)
					doBranchLeaves(world, upPos.down(2), rand, false, 2, true);
				
				if (i > base + 1)
					doBranchLeaves(world, upPos.down(), rand, false, 3, true);
				
				doBranchLeaves(world, upPos, rand, (i >= height - 1), 4, true);
			}
		}
	}
}
