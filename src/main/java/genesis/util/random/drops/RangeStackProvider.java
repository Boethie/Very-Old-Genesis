package genesis.util.random.drops;

import java.util.Random;

import genesis.util.random.i.IntRange;
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
	public int get(Random rand)
	{
		return range.get(rand);
	}
}
