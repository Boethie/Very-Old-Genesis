package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenTreeCordaites extends WorldGenTreeBase
{
	public WorldGenTreeCordaites(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.CORDAITES).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.CORDAITES),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		Block soil = world.getBlockState(pos.down()).getBlock();
		
		if (
				soil == null 
				|| !soil.canSustainPlant(world, pos, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.CORDAITES))
				/*|| !world.getBlockState(pos).getBlock().isAir(world, pos)*/)
		{
			return false;
		}
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight);
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		int baseHeight = 2 + rand.nextInt(3);
		
		for (int i = baseHeight; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos basePos = pos.up(baseHeight - 1);
		generateBaseBranch(world, basePos, rand, baseHeight, 1, 0);
		generateBaseBranch(world, basePos, rand, baseHeight, -1, 0);
		generateBaseBranch(world, basePos, rand, baseHeight, 0, 1);
		generateBaseBranch(world, basePos, rand, baseHeight, 0, -1);
		
		BlockPos branchPos = pos.up(treeHeight);
		
		branchPos = branchPos.add(0, -1, 0);
		
		doBranchLeaves(world, branchPos, rand, true, 1);
		
		float percent;
		int leaves;
		int leavesLevel = ((pos.getY() + this.minHeight - 5) < basePos.getY())? basePos.getY() + 2 : pos.getY() + this.minHeight - 5;
		
		while (branchPos.getY() > leavesLevel)
		{
			branchPos = branchPos.add(0, -1, 0);
			
			percent = 1.0F - (((float)branchPos.getY() - (float)leavesLevel) / ((float)pos.getY() + (float)treeHeight - (float)leavesLevel));
			leaves = MathHelper.ceiling_float_int(4.0F * percent);
			
			if (leaves > 4)
				leaves = 4;
			
			doBranchLeaves(world, branchPos, rand, false, leaves);
		}
		
		return true;
	}
	
	private void generateBaseBranch(World world, BlockPos pos, Random rand, int baseHeight, int dirX, int dirZ)
	{
		int spread = 1;
		
		for (int i = 0; i < baseHeight; ++i)
		{
			if (i > 0 && rand.nextInt(2) == 0)
				++spread;
			
			setBlockInWorld(world, pos.add(spread * dirX, -i, spread * dirZ), wood, true);
		}
	}
}
