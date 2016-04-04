package genesis.util.random.i;

import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.WeightedRandom;

public class WeightedIntProvider implements RandomIntProvider
{
	private final ImmutableList<WeightedIntItem> itemList;
	private final int total;
	
	public WeightedIntProvider(WeightedIntItem... items)
	{
		this.itemList = ImmutableList.copyOf(items);
		this.total = WeightedRandom.getTotalWeight(itemList);
	}
	
	@Override
	public int get(Random rand)
	{
		return WeightedRandom.getRandomItem(rand, itemList, total).get(rand);
	}
}
