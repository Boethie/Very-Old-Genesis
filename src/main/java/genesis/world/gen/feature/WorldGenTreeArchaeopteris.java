package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeArchaeopteris extends WorldGenTreeBase
{
	public WorldGenTreeArchaeopteris(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.ARCHAEOPTERIS), 
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.ARCHAEOPTERIS), 
				notify);
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos)
	{
		return false;
	}
}
