package genesis.util;

import com.google.common.base.Predicate;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@FunctionalInterface
public interface WorldBlockMatcher
{
	public static final WorldBlockMatcher TRUE = (s, w, p) -> true;
	
	public static final WorldBlockMatcher REPLACEABLE = (s, w, p) -> s.getBlock().isReplaceable(w, p);
	public static final WorldBlockMatcher LEAVES = (s, w, p) -> s.getBlock().isLeaves(s, w, p);
	public static final WorldBlockMatcher WATER = (s, w, p) -> s.getMaterial() == Material.water;
	
	public static final WorldBlockMatcher STANDARD_AIR_WATER = or(REPLACEABLE, LEAVES);
	public static final WorldBlockMatcher STANDARD_AIR = and(STANDARD_AIR_WATER, not(WATER));
	
	public static final WorldBlockMatcher SOLID_TOP = solidSide(EnumFacing.UP);
	
	public static WorldBlockMatcher state(Predicate<IBlockState> predicate)
	{
		return (s, w, p) -> predicate.apply(s);
	}
	
	public static WorldBlockMatcher not(WorldBlockMatcher matcher)
	{
		return (s, w, p) -> !matcher.apply(s, w, p);
	}
	
	public static WorldBlockMatcher and(WorldBlockMatcher... matchers)
	{
		return (s, w, p) ->
		{
			for (WorldBlockMatcher matcher : matchers)
				if (!matcher.apply(s, w, p))
					return false;
			return true;
		};
	}
	
	public static WorldBlockMatcher or(WorldBlockMatcher... matchers)
	{
		return (s, w, p) ->
		{
			for (WorldBlockMatcher matcher : matchers)
				if (matcher.apply(s, w, p))
					return true;
			return false;
		};
	}
	
	public static WorldBlockMatcher solidSide(EnumFacing side)
	{
		return (s, w, p) -> s.getBlock().isSideSolid(s, w, p, side);
	}
	
	public boolean apply(IBlockState state, IBlockAccess world, BlockPos pos);
	
	public default boolean apply(IBlockAccess world, BlockPos pos)
	{
		return apply(world.getBlockState(pos), world, pos);
	}
}
