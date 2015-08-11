package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeArchaeopteris extends WorldGenTreeBase
{
	public WorldGenTreeArchaeopteris(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.ARCHAEOPTERIS).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.ARCHAEOPTERIS),
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
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight) - 5;
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight - 1);
		
		int branchY = 3 + rand.nextInt(4);
		int branchBaseHeight = 2;
		int branchGrowSize = 3;
		int leavesLength = 3;
		
		doPineTopLeaves(world, pos, branchPos, treeHeight, branchPos.down(branchY).getY(), rand, false);
		
		if (rand.nextInt(2) == 0)
			generateBranchSideup(world, branchPos.down(branchY + rand.nextInt(2)), rand, 1, 0, branchBaseHeight, branchGrowSize, leavesLength);
		if (rand.nextInt(2) == 0)
			generateBranchSideup(world, branchPos.down(branchY + rand.nextInt(2)), rand, -1, 0, branchBaseHeight, branchGrowSize, leavesLength);
		if (rand.nextInt(2) == 0)
			generateBranchSideup(world, branchPos.down(branchY + rand.nextInt(2)), rand, 0, 1, branchBaseHeight, branchGrowSize, leavesLength);
		if (rand.nextInt(2) == 0)
			generateBranchSideup(world, branchPos.down(branchY + rand.nextInt(2)), rand, 0, -1, branchBaseHeight, branchGrowSize, leavesLength);
		
		branchPos = branchPos.down(branchY - 2);
		
		if (rand.nextInt(2) == 0)
			generateBranchSide(world, branchPos.down(rand.nextInt(4)), rand, 1, 0, 1 + rand.nextInt(3), 6);
		if (rand.nextInt(2) == 0)
			generateBranchSide(world, branchPos.down(rand.nextInt(4)), rand, -1, 0, 1 + rand.nextInt(3), 6);
		if (rand.nextInt(2) == 0)
			generateBranchSide(world, branchPos.down(rand.nextInt(4)), rand, 0, 1, 1 + rand.nextInt(3), 6);
		if (rand.nextInt(2) == 0)
			generateBranchSide(world, branchPos.down(rand.nextInt(4)), rand, 0, -1, 1 + rand.nextInt(3), 6);
		
		return true;
	}
}
