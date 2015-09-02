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
		
		if (rand.nextInt(rarity) != 0)
			return false;
		
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
		
		doPineTopLeaves(world, pos, branchPos, treeHeight, branchPos.down(branchY).getY(), rand, false);
		
		return true;
	}
}
