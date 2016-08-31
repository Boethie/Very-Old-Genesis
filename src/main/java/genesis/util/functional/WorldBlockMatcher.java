package genesis.util.functional;

import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@FunctionalInterface
public interface WorldBlockMatcher
{
	WorldBlockMatcher TRUE = (s, w, p) -> true;
	
	WorldBlockMatcher REPLACEABLE = (s, w, p) -> s.getBlock().isReplaceable(w, p);
	WorldBlockMatcher LEAVES = (s, w, p) -> s.getBlock().isLeaves(s, w, p);
	WorldBlockMatcher WATER = (s, w, p) -> s.getMaterial() == Material.WATER;
	
	WorldBlockMatcher STANDARD_AIR_WATER = or(REPLACEABLE, LEAVES);
	WorldBlockMatcher STANDARD_AIR = and(STANDARD_AIR_WATER, not(WATER));	// REPLACEABLE includes WATER.
	
	WorldBlockMatcher SOLID_TOP = solidSide(EnumFacing.UP);
	
	static WorldBlockMatcher state(Predicate<IBlockState> predicate)
	{
		return (s, w, p) -> predicate.test(s);
	}
	
	static WorldBlockMatcher state(IBlockState state)
	{
		return state((s) -> s.equals(state));
	}
	
	static WorldBlockMatcher block(Block block)
	{
		return state((s) -> s.getBlock() == block);
	}
	
	static WorldBlockMatcher not(WorldBlockMatcher matcher)
	{
		return (s, w, p) -> !matcher.apply(s, w, p);
	}
	
	static WorldBlockMatcher and(WorldBlockMatcher... matchers)
	{
		return (s, w, p) ->
		{
			for (WorldBlockMatcher matcher : matchers)
				if (!matcher.apply(s, w, p))
					return false;
			return true;
		};
	}
	
	static WorldBlockMatcher or(WorldBlockMatcher... matchers)
	{
		return (s, w, p) ->
		{
			for (WorldBlockMatcher matcher : matchers)
				if (matcher.apply(s, w, p))
					return true;
			return false;
		};
	}
	
	static WorldBlockMatcher solidSide(EnumFacing side)
	{
		return (s, w, p) -> s.getBlock().isSideSolid(s, w, p, side);
	}
	
	boolean apply(IBlockState state, IBlockAccess world, BlockPos pos);
	
	default boolean apply(IBlockAccess world, BlockPos pos)
	{
		return apply(world.getBlockState(pos), world, pos);
	}
}
