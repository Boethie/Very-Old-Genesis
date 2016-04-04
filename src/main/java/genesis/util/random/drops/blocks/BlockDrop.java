package genesis.util.random.drops.blocks;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.util.random.i.IntRange;

public abstract class BlockDrop implements BlockStackProvider
{
	protected final IntRange range;
	
	public BlockDrop(int min, int max)
	{
		range = IntRange.create(min, max);
	}
	
	public BlockDrop(int size)
	{
		range = IntRange.create(size);
	}
	
	@Override
	@Deprecated
	public ItemStack getStack(int size)
	{
		return null;
	}
	
	@Deprecated
	@Override
	public ItemStack getStack(Random rand)
	{
		return null;
	}
	
	@Override
	public int get(Random rand)
	{
		return range.get(rand);
	}
	
	@Override
	public ItemStack getStack(IBlockState state, Random rand)
	{
		return getStack(state, get(rand));
	}
}