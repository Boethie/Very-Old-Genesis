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
import net.minecraft.world.World;

public class WorldGenTreeSigillaria extends WorldGenTreeBase
{
	public WorldGenTreeSigillaria(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.SIGILLARIA).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.SIGILLARIA),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		Block soil = world.getBlockState(pos.down()).getBlock();
		
		if (soil == null || !soil.canSustainPlant(world, pos, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.SIGILLARIA)))
		{
			return false;
		}
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight) - 5;
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight);
		int leaves = 1;
		
		if (rand.nextInt(8) > 4)
		{
			doBranch(world, branchPos, 0, 0, rand, leaves, true);
		}
		else
		{
			if (rand.nextInt(2) == 0)
			{
				doBranch(world, branchPos, 1, 0, rand, leaves);
				doBranch(world, branchPos, -1, 0, rand, leaves);
			}
			else
			{
				doBranch(world, branchPos, 0, 1, rand, leaves);
				doBranch(world, branchPos, 0, -1, rand, leaves);
			}
		}
		
		return true;
	}
	
	private boolean doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength)
	{
		return doBranch(world, pos, dirX, dirZ, random, leaveLength, false);
	}
	
	private boolean doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength, boolean forceLongBranch)
	{
		boolean generated = false;
		
		pos = pos.add((1 * dirX), 0, (1 * dirZ));
		setBlockInWorld(world, pos, wood);
		
		pos = pos.add((1 * dirX), 1, (1 * dirZ));
		setBlockInWorld(world, pos, wood);
		
		if (random.nextInt(2) == 0 || forceLongBranch)
		{
			pos = pos.add(0, 1, 0);
			setBlockInWorld(world, pos, wood);
			doBranchLeaves(world, pos, random, false, leaveLength);
		}
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, wood);
		doBranchLeaves(world, pos, random, true, leaveLength);
		
		generated = true;
		
		return generated;
	}
}
