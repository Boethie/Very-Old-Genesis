package genesis.world.biome.decorate;

import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenRoots extends WorldGenDecorationBase
{
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		
		if (
				block != Blocks.dirt
				|| !world.getBlockState(pos.down()).getBlock().isAir(world, pos))
		{
			return false;
		}
		
		int radius = 6;
		int depth = 3;
		int length = 1 + random.nextInt(5);
		
		if (!(
				findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.ARCHAEOPTERIS), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.LEPIDODENDRON), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.SIGILLARIA), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.CORDAITES), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.PSARONIUS), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.VOLTZIA), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.ARAUCARIOXYLON), radius, depth, radius)))
		{
			return false;
		}
		
		for (int i = 0; i < length; ++i)
		{
			if (world.getBlockState(pos.down(i + 1)).getBlock().isAir(world, pos.down(i + 1)))
				setBlockInWorld(world, pos.down(i + 1), GenesisBlocks.roots.getDefaultState());
			else
				break;
		}
		
		return true;
	}
}
