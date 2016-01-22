package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeFicus extends WorldGenTreeBase
{
	public WorldGenTreeFicus(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.FICUS).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.FICUS),
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
		int base = 1 + rand.nextInt(3);
		
		if (!isCubeClear(world, pos.up(base), 5, treeHeight))
		{
			return false;
		}
		
		int mainBranches = 1 + rand.nextInt(8);
		
		for (int i = 0; i < mainBranches; ++i)
			branchUp(world, pos, rand, treeHeight, base);
		
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
			if (rand.nextInt(2) == 0 && i > base && fallCount < 4)
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
			
			if (i > height - (3 + rand.nextInt(3)))
				doBranchLeaves(world, upPos, rand, (i >= height - 1), 4, true);
		}
	}
}
