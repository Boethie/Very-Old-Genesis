package genesis.world.gen.feature;

import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumSilt;
import genesis.metadata.EnumTree;
import genesis.metadata.SiltBlocks;
import genesis.metadata.TreeBlocksAndItems;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeVoltzia extends WorldGenTreeBase
{
	public WorldGenTreeVoltzia (int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.VOLTZIA).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.VOLTZIA),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		
		this.addAllowedBlocks(GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.SILT).getBlockState(), GenesisBlocks.silt.getBlock(SiltBlocks.SILT, EnumSilt.RED_SILT).getBlockState(), Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT).getBlock().getBlockState());
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
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight - 2);
		
		doPineTopLeaves(world, pos, branchPos, treeHeight, pos.getY() + 1, rand, false, 2, true, false);
		
		return true;
	}
}
