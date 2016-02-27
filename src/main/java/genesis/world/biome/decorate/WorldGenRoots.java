package genesis.world.biome.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenRoots extends WorldGenDecorationBase
{
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		
		pos = new BlockPos(pos.getX(), 80, pos.getZ());
		
		List<IBlockState> allowedBlocks = new ArrayList<IBlockState>();
		
		allowedBlocks.add(Blocks.dirt.getDefaultState());
		allowedBlocks.add(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
		allowedBlocks.add(GenesisBlocks.moss.getDefaultState());
		
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (
					allowedBlocks.contains(block.getDefaultState())
					&& world.getBlockState(pos.down()).getBlock().isAir(world, pos.down()))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 55);
		
		boolean generated = false;
		
		int radius = 4;
		int depth = 2;
		int length = 1 + random.nextInt(2);
		
		if (!(
				findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.ARCHAEOPTERIS), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.SIGILLARIA), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.LEPIDODENDRON), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.CORDAITES), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.PSARONIUS), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.VOLTZIA), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.ARAUCARIOXYLON), radius, depth, radius)
				|| findBlockInRange(world, pos, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.LOG, EnumTree.METASEQUOIA), radius, depth, radius)))
		{
			return false;
		}
		
		for (int i = 0; i < length; ++i)
		{
			if (world.getBlockState(pos.down(i + 1)).getBlock().isAir(world, pos.down(i + 1)))
			{
				setBlockInWorld(world, pos.down(i + 1), GenesisBlocks.roots.getDefaultState());
				generated = true;
			}
			else
			{
				break;
			}
		}
		
		return generated;
	}
}
