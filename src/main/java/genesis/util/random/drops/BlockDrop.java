package genesis.util.random.drops;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import genesis.util.random.*;

public abstract class BlockDrop extends IntRange
{
	protected final IntRange range;
	
	public BlockDrop(int min, int max)
	{
		range = IntRange.create(min, max);
	}
	
	public abstract ItemStack getStack(IBlockState state, int size);
	public abstract ItemStack getStack(IBlockState state, Random rand);
	
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