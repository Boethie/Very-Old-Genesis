package genesis.util.random.drops;

import java.util.Random;

import genesis.util.random.IntRange;
import net.minecraft.item.ItemStack;

public class RangeStackProvider extends AbstractStackProvider
{
	protected final IntRange range;
	
	public RangeStackProvider(ItemStack stack, IntRange range)
	{
		super(stack);
		
		this.range = range;
	}
	
	public RangeStackProvider(ItemStack stack, int min, int max)
	{
		this(stack, IntRange.create(min, max));
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
