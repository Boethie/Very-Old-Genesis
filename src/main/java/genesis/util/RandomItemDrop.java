package genesis.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class RandomItemDrop
{
	public Item item;
	public int min;
	public int max;
	
	public RandomItemDrop(Item itemIn, int minIn, int maxIn)
	{
		item = itemIn;
		min = minIn;
		max = maxIn;
	}
	
	/**
	 * Gets an ItemStack with an item count between min and max.
	 * @param rand
	 * @return A random ItemStack.
	 */
	public ItemStack getRandom(Random rand)
	{
		int amt = min;
		
		if (max != min)
		{
			amt = MathHelper.getRandomIntegerInRange(rand, min, max);
		}
		
		return new ItemStack(item, amt);
	}
	
	/**
	 * Gets an ItemStack with a size of 1.
	 * @return
	 */
	public ItemStack getOne()
	{
		return new ItemStack(item);
	}
}
