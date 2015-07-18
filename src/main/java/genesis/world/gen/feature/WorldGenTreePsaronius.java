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

public class WorldGenTreePsaronius extends WorldGenTreeBase
{
	public WorldGenTreePsaronius(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.PSARONIUS).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.PSARONIUS),
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
				|| !soil.canSustainPlant(world, pos, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.PSARONIUS))
				|| !world.getBlockState(pos).getBlock().isAir(world, pos))
		{
			return false;
		}
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight) - 1;
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight - 1);
		
		doLeavesBranch(world, branchPos, 1, 0, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, -1, 0, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, 0, 1, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, 0, -1, rand, 2 + rand.nextInt(3));
		
		doLeavesBranch(world, branchPos, 1, 1, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, -1, 1, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, 1, -1, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, -1, -1, rand, 2 + rand.nextInt(3));
		
		doLeavesCap(world, branchPos, rand);
		
		return true;
	}
	
	private void doLeavesBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int length)
	{
		//BlockPos backLeaves;
		
		for (int i = 1; i <= length; ++ i)
		{
			pos = pos.add((1 * dirX), 0, (1 * dirZ));
			setBlockInWorld(world, pos, leaves);
		}
	}
	
	private void doLeavesCap(World world, BlockPos pos, Random random)
	{
		pos = pos.add(0, 1, 0);
		//setBlockInWorld(world, pos, leaves);
		setBlockInWorld(world, pos.north(), leaves);
		setBlockInWorld(world, pos.south(), leaves);
		setBlockInWorld(world, pos.east(), leaves);
		setBlockInWorld(world, pos.west(), leaves);
		/*
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, leaves);
		
		if (random.nextInt(2) == 0)
		{
			pos = pos.add(0, 1, 0);
			setBlockInWorld(world, pos, leaves);
		}
		*/
	}
}
