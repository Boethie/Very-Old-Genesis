package genesis.util.random.drops.blocks;

import java.util.Random;

import net.minecraft.item.ItemStack;
import genesis.util.random.*;

public abstract class BlockDrop implements BlockStackProvider
{
	protected final IntRange range;
	
	public BlockDrop(int min, int max)
	{
		range = IntRange.create(min, max);
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
	public Integer get(Random rand)
	{
		return range.get(rand);
	}
	
	@Override
	public Integer getMin()
	{
		return range.getMin();
	}
	
	@Override
	public Integer getMax()
	{
		return range.getMax();
	}
}