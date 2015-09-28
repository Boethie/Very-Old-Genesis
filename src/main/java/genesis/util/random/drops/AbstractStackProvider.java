package genesis.util.random.drops;

import java.util.Random;

import net.minecraft.item.ItemStack;

public abstract class AbstractStackProvider implements StackProvider
{
	protected final ItemStack stack;
	
	public AbstractStackProvider(ItemStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public ItemStack getStack(int size)
	{
		ItemStack out = stack.copy();
		out.stackSize = size;
		return out;
	}
	
	@Override
	public ItemStack getStack(Random rand)
	{
		return getStack(get(rand));
	}
}
