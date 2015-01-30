package genesis.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
	
	public ItemStack getRandom(Random rand)
	{
		int amt = min;
		
		if (max != min)
		{
			amt = rand.nextInt((max - min) + 1) + min;
		}
		
		return new ItemStack(item, amt);
	}
	
	public ItemStack getOne()
	{
		return new ItemStack(item);
	}
}
