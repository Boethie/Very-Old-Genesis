package genesis.util.functional;

import java.util.Collection;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class StateMatcher
{
	public static Predicate<IBlockState> forBlocks(Collection<? extends Block> blocks)
	{
		return (state) -> blocks.contains(state.getBlock());
	}
	
	public static Predicate<IBlockState> forBlocks(Block... blocks)
	{
		return forBlocks(ImmutableSet.copyOf(blocks));
	}
	
	public static Predicate<IBlockState> forStates(Collection<? extends IBlockState> states)
	{
		return states::contains;
	}
	
	public static Predicate<IBlockState> forStates(IBlockState... states)
	{
		return forStates(ImmutableSet.copyOf(states));
	}
}
