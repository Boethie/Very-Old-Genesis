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

public class WorldGenTreeAraucarioxylon extends WorldGenTreeBase
{
	public WorldGenTreeAraucarioxylon(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.ARAUCARIOXYLON).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.ARAUCARIOXYLON),
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
				|| !soil.canSustainPlant(world, pos, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.LEPIDODENDRON))
				|| !world.getBlockState(pos).getBlock().isAir(world, pos))
		{
			return false;
		}
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight);
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight);
		
		doBranchLeaves(world, branchPos.down(), rand, true, 1);
		
		boolean alternate = false;
		float percent;
		int leaves;
		
		while (branchPos.getY() > pos.getY())
		{
			branchPos = branchPos.add(0, -1, 0);
			
			percent = ((float)(pos.getY() + treeHeight) - (float)branchPos.getY()) / (float)treeHeight;
			leaves = MathHelper.ceiling_float_int(4.0F * percent);
			
			if (leaves > 4)
				leaves = 4;
			
			if (alternate)
			{
				doBranchLeaves(world, branchPos, rand, false, leaves);
			}
			
			alternate = !alternate;
		}
		
		return true;
	}
}
