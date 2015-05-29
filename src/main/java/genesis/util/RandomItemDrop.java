package genesis.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class RandomItemDrop extends RandomIntRange
{
	public Item item;
	
	public RandomItemDrop(Item itemIn, int minIn, int maxIn)
	{
		super(minIn, maxIn);
		
		item = itemIn;
	}
	
	/**
	 * Gets an ItemStack with an item count between min and max.
	 * @param rand
	 * @return A random ItemStack.
	 */
	public ItemStack getRandomStack(Random rand)
	{
		return new ItemStack(item, getRandom(rand));
	}
	
	/**
	 * Gets an ItemStack with a size of 1.
	 * @return
	 */
	public ItemStack getOneInStack()
	{
		return new ItemStack(item);
	}
}
