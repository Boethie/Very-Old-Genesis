package genesis.world.iworldgenerators;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

public class WorldGenHelper
{
	public static BlockPos findSurface(World world, BlockPos pos)
	{
		while (isGround(world, pos))
			pos = pos.up();
		
		while (!isGround(world, pos.down()))
			pos = pos.down();
		
		return pos;
	}
	
	public static boolean isGround(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (block.isReplaceable(world, pos))
			return false;
		if (!state.isSideSolid(world, pos, EnumFacing.UP))
			return false;
		if (block.isWood(world, pos))
			return false;
		if (block.isLeaves(state, world, pos))
			return false;
		
		return true;
	}
	
	public static class RandomStates
	{
		private final List<RandomState> states;
		private final int total;
		
		public RandomStates(List<RandomState> states)
		{
			this.states = ImmutableList.copyOf(states);
			total = WeightedRandom.getTotalWeight(states);
		}
		
		public RandomStates(RandomState... states)
		{
			this(ImmutableList.copyOf(states));
		}
		
		public IBlockState getState(Random random)
		{
			return WeightedRandom.getRandomItem(random, states, total).state;
		}
	}
	
	public static class RandomState extends WeightedRandom.Item
	{
		public final IBlockState state;
		
		public RandomState(IBlockState state, int weight)
		{
			super(weight);
			this.state = state;
		}
	}
}
