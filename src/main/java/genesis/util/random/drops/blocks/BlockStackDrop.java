package genesis.util.random.drops.blocks;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockStackDrop extends BlockDrop
{
	protected final ItemStack stack;
	
	public BlockStackDrop(ItemStack stack, int min, int max)
	{
		super(min, max);
		this.stack = stack.copy();
	}
	
	public BlockStackDrop(Item item, int min, int max)
	{
		this(new ItemStack(item), min, max);
	}
	
	public BlockStackDrop(ItemStack stack, int size)
	{
		this(stack, size, size);
	}
	
	public BlockStackDrop(Item item, int size)
	{
		this(item, size, size);
	}
	
	/**
	 * @return The base ItemStack.
	 */
	public ItemStack getStack()
	{
		return stack.copy();
	}
	
	/**
	 * @param size The size of the stack to return.
	 * @return A stack using the base item and metadata with the size provided.
	 */
	@Override
	public ItemStack getStack(IBlockState state, int size)
	{
		ItemStack newStack = stack.copy();
		newStack.stackSize = size;
		return newStack;
	}
	
	/**
	 * Gets an ItemStack with an item count between min and max.
	 * @param rand The RNG to use.
	 * @return A random ItemStack.
	 */
	@Override
	public ItemStack getStack(IBlockState state, Random rand)
	{
		return getStack(state, get(rand));
	}
}