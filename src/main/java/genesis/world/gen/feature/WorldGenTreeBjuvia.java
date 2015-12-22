package genesis.world.gen.feature;

import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.EnumTree;
import genesis.metadata.SiltBlocks;
import genesis.metadata.TreeBlocksAndItems;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeBjuvia extends WorldGenTreeBase
{
	public WorldGenTreeBjuvia(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.BJUVIA).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.BJUVIA),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		
		this.addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT).getBlockState(), GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.RED_SILT).getBlockState());
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		pos = getTreePos(world, pos);
		
		if (!canTreeGrow(world, pos))
			return false;
		
		if (rand.nextInt(rarity) != 0)
			return false;
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight) - 1;
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight);
		
		//setBlockInWorld(world, branchPos, leaves);
		
		doLeavesBranch(world, branchPos, 1, 0, rand, 1 + rand.nextInt(2));
		doLeavesBranch(world, branchPos, -1, 0, rand, 1 + rand.nextInt(2));
		doLeavesBranch(world, branchPos, 0, 1, rand, 1 + rand.nextInt(2));
		doLeavesBranch(world, branchPos, 0, -1, rand, 1 + rand.nextInt(2));
		
		doLeavesBranch(world, branchPos, 1, 1, rand, 1 + rand.nextInt(2));
		doLeavesBranch(world, branchPos, -1, 1, rand, 1 + rand.nextInt(2));
		doLeavesBranch(world, branchPos, 1, -1, rand, 1 + rand.nextInt(2));
		doLeavesBranch(world, branchPos, -1, -1, rand, 1 + rand.nextInt(2));
		
		return true;
	}
	
	private void doLeavesBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int length)
	{
		for (int i = 1; i <= length; ++ i)
		{
			pos = pos.add((dirX), 0, (dirZ));
			
			if (i == length)
				pos = pos.add(0, -1, 0);
			
			setBlockInWorld(world, pos, leaves);
		}
	}
}
