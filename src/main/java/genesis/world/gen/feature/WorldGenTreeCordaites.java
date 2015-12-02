package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
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
		
		setCanGrowInWater(true);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		pos = getTreePos(world, pos);
		
		if (!canTreeGrow(world, pos))
			return false;
		
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
		
		BlockPos branchPos = pos.up(treeHeight - 1);
		
		int leavesLevel = ((pos.getY() + this.minHeight - 5) < basePos.getY())? basePos.getY() + 2 : pos.getY() + this.minHeight - 5;
		
		leavesLevel += rand.nextInt(3);
		
		branchPos = pos.up(treeHeight - 3);
		//int distFromTop = branchPos.getY() - leavesLevel;
		//int maxBranchLength = 3;
		/*
		if (distFromTop > 0)
		{
			if (rand.nextInt(100) > 10)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 1, 0, 1 + rand.nextInt(maxBranchLength), 2);
			if (rand.nextInt(100) > 10)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, -1, 0, 1 + rand.nextInt(maxBranchLength), 2);
			if (rand.nextInt(100) > 10)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 0, 1, 1 + rand.nextInt(maxBranchLength), 2);
			if (rand.nextInt(100) > 10)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 0, -1, 1 + rand.nextInt(maxBranchLength), 2);
			
			distFromTop++;
			
			if (rand.nextInt(100) > 10)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 1, 0, 1 + rand.nextInt(maxBranchLength - 1), 2);
			if (rand.nextInt(100) > 10)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, -1, 0, 1 + rand.nextInt(maxBranchLength - 1), 2);
			if (rand.nextInt(100) > 10)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 0, 1, 1 + rand.nextInt(maxBranchLength - 1), 2);
			if (rand.nextInt(100) > 10)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 0, -1, 1 + rand.nextInt(maxBranchLength - 1), 2);
			
			distFromTop += 2;
			
			if (rand.nextInt(100) > 50)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 1, 0, 1 + rand.nextInt(maxBranchLength - 2), 2);
			if (rand.nextInt(100) > 50)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, -1, 0, 1 + rand.nextInt(maxBranchLength - 2), 2);
			if (rand.nextInt(100) > 50)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 0, 1, 1 + rand.nextInt(maxBranchLength - 2), 2);
			if (rand.nextInt(100) > 50)
				generateBranchSide(world, branchPos.down(rand.nextInt(distFromTop)), rand, 0, -1, 1 + rand.nextInt(maxBranchLength - 2), 2);
		}
		*/
		branchPos = branchPos.up(2);
		
		doPineTopLeaves(world, pos, branchPos, treeHeight, leavesLevel, rand, false, 3, false, false);
		branchPos = new BlockPos(pos.getX(), leavesLevel, pos.getZ());
		
		int leaves = 1 + rand.nextInt(2);
		
		if (rand.nextInt(2) == 0)
		{
			doBranch(world, branchPos, 1, 0, rand, leaves, true);
			doBranch(world, branchPos, -1, 0, rand, leaves, true);
			doBranch(world, branchPos, 0, 1, rand, leaves, true);
			doBranch(world, branchPos, 0, -1, rand, leaves, true);
		}
		else
		{
			doBranch(world, branchPos, 1, 1, rand, leaves, true);
			doBranch(world, branchPos, -1, -1, rand, leaves, true);
			doBranch(world, branchPos, -1, 1, rand, leaves, true);
			doBranch(world, branchPos, 1, -1, rand, leaves, true);
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
	
	private boolean doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength, boolean leaveBranch)
	{
		pos = pos.add((1 * dirX), 0, (1 * dirZ));
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, false, leaveLength);
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, false, leaveLength);
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, true, leaveLength);
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, true, leaveLength);
		
		return true;
	}
}
