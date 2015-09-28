package genesis.util.random.drops;

import java.util.Random;

import net.minecraft.item.ItemStack;

public class DecimalStackProvider extends AbstractStackProvider
{
	protected final float stackSize;
	
	public DecimalStackProvider(ItemStack stack, float stackSize)
	{
		super(stack);
		
		this.stackSize = stackSize;
	}
	
	@Override
	public Integer get(Random rand)
	{
		int out = getMin();
		
		if (rand.nextFloat() <= stackSize - out)
		{
			out++;
		}
		
		return out;
	}
	
	@Override
	public Integer getMin()
	{
		return (int) Math.floor(stackSize);
	}
	
	@Override
	public Integer getMax()
	{
		return stackSize % 0 == 0 ? getMin() : getMin() + 1;
	}
}
