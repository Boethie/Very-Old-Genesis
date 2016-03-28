package genesis.world.iworldgenerators;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WorldGenHelper
{
	public static BlockPos findSurface(World world, BlockPos pos) {
		while (isGround(world, pos))
		{
			pos = pos.up();
		}
		while (!isGround(world, pos.down()))
		{
			pos = pos.down();
		}
		return pos;
	}
	
	public static boolean isGround(World world, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		return !(block.isReplaceable(world, pos) || !block.isFullCube() || block.isWood(world, pos) || block.isLeaves(world, pos));
	}
	
	public static class RandomStates
	{
		private ArrayList<RandomState> blocks = new ArrayList<RandomState>();
		
		public RandomStates(RandomState... blocks)
		{
			Collections.addAll(this.blocks, blocks);
		}
		
		public IBlockState getState(Random random)
		{
			return WeightedRandom.getRandomItem(random, blocks).state;
		}
	}
	
	public static class RandomState extends WeightedRandom.Item
	{
		private IBlockState state;
		
		public RandomState(IBlockState state, int weight)
		{
			super(weight);
			this.state = state;
		}
	}
}
