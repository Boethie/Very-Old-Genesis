package genesis.util.functional;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class StateMatcher
{
	public static Predicate<IBlockState> forBlocks(Collection<? extends Block> blocks)
	{
		return (state) -> {
			return blocks.contains(state.getBlock());
		};
	}
	
	public static Predicate<IBlockState> forBlocks(Block... blocks)
	{
		return forBlocks(ImmutableSet.copyOf(blocks));
	}
	
	public static Predicate<IBlockState> forStates(Collection<? extends IBlockState> states)
	{
		return (state) -> {
			return states.contains(state);
		};
	}
	
	public static Predicate<IBlockState> forStates(IBlockState... states)
	{
		return forStates(ImmutableSet.copyOf(states));
	}
}
