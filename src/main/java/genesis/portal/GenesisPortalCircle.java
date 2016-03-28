package genesis.portal;

import com.google.common.collect.Lists;
import genesis.common.GenesisBlocks;
import genesis.world.iworldgenerators.WorldGenHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

import static genesis.world.iworldgenerators.WorldGenHelper.findSurface;

public class GenesisPortalCircle
{
	private static WorldGenHelper.RandomStates genesisRocks = new WorldGenHelper.RandomStates(
			new WorldGenHelper.RandomState(GenesisBlocks.granite.getDefaultState(), 3),
			new WorldGenHelper.RandomState(GenesisBlocks.mossy_granite.getDefaultState(), 1));
	
	private static WorldGenHelper.RandomStates vanillaRocks = new WorldGenHelper.RandomStates(
			new WorldGenHelper.RandomState(Blocks.cobblestone.getDefaultState(), 3),
			new WorldGenHelper.RandomState(Blocks.mossy_cobblestone.getDefaultState(), 1));
	
	private static ArrayList<BlockPos> posts = Lists.newArrayList(
			new BlockPos(-1, 0, -8), new BlockPos( 1, 0, -8), new BlockPos(-4, 0, -7), new BlockPos( 4, 0, -7), new BlockPos(0, -6, -6),
			new BlockPos(-1, 0,  8), new BlockPos( 1, 0,  8), new BlockPos(-4, 0,  7), new BlockPos( 4, 0,  7), new BlockPos(0, -6,  6),
			new BlockPos(-8, 0, -1), new BlockPos(-8, 0,  1), new BlockPos(-7, 0, -4), new BlockPos(-7, 0,  4), new BlockPos(0,  6, -6),
			new BlockPos( 8, 0, -1), new BlockPos( 8, 0,  1), new BlockPos( 7, 0, -4), new BlockPos( 7, 0,  4), new BlockPos(0,  6,  6));
	
	private static ArrayList<BlockPos> buds = Lists.newArrayList(
			new BlockPos(-4, 0, -4), new BlockPos(4, 0, -4), new BlockPos(-4, 0, 4), new BlockPos(4, 0, 4));
	
	public static void genStructure(World world, BlockPos center, boolean isNew)
	{
		WorldGenHelper.RandomStates rocks = isNew ? genesisRocks : vanillaRocks;
		
		for (BlockPos post : posts)
		{
			BlockPos ground = findSurface(world, center.add(post));
			if (Math.abs(ground.getY() - center.getY()) > 5)
			{
				continue;
			}
			int height = ground.getY() + 3 - (isNew ? 0 : world.rand.nextInt(2));
			while (ground.getY() < height)
			{
				if (!world.getBlockState(ground).getBlock().isReplaceable(world, ground))
				{
					break;
				}
				world.setBlockState(ground, rocks.getState(world.rand));
				ground = ground.up();
			}
		}
		if (isNew)
		{
			for (BlockPos bud : buds)
			{
				world.setBlockState(findSurface(world, center.add(bud)), rocks.getState(world.rand));
			}
		}
	}
	
}
