package genesis.util.range;

import genesis.util.range.IntRange.Range;

import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class RandomDrop
{
	public static class RandomStackDrop extends IntRange.Range
	{
		protected ItemStack stack;
		
		public RandomStackDrop(ItemStack stack, int min, int max)
		{
			super(min, max);
			
			this.stack = stack;
		}
		
		public RandomStackDrop(Item item, int min, int max)
		{
			this(new ItemStack(item), min, max);
		}
		
		public RandomStackDrop(ItemStack stack)
		{
			this(stack, 1, 1);
		}
		
		public RandomStackDrop(Item item)
		{
			this(item, 1, 1);
		}
		
		/**
		 * @return The base ItemStack.
		 */
		public ItemStack getStack()
		{
			return stack;
		}
		
		/**
		 * @param size The size of the stack to return.
		 * @return A stack using the base item and metadata with the size provided.
		 */
		public ItemStack getStackWithSize(int size)
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
		public ItemStack getRandomStack(Random rand)
		{
			return getStackWithSize(get(rand));
		}
	}
	
	protected ImmutableList<RandomStackDrop> drops;
	
	public RandomDrop(RandomStackDrop... drops)
	{
		this.drops = ImmutableList.copyOf(drops);
	}
	
	public RandomDrop(ItemStack stack, int min, int max)
	{
		this(new RandomStackDrop(stack, min, max));
	}
	
	public RandomDrop(Item item, int min, int max)
	{
		this(new ItemStack(item), min, max);
	}
	
	public RandomDrop(ItemStack stack)
	{
		this(stack, 1, 1);
	}
	
	public RandomDrop(Item item)
	{
		this(item, 1, 1);
	}
	
	public ImmutableList<RandomStackDrop> getDrops()
	{
		return drops;
	}
	
	public RandomStackDrop getRandomDrop(Random rand)
	{
		return drops.get(rand.nextInt(drops.size()));
	}
	
	public ItemStack getRandomStackDrop(Random rand)
	{
		return getRandomDrop(rand).getRandomStack(rand);
	}
}
