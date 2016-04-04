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
	public int get(Random rand)
	{
		int out = (int) Math.floor(stackSize);
		
		if (rand.nextFloat() <= stackSize - out)
		{
			out++;
		}
		
		return out;
	}
}
