package genesis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.WeightedRandom;

public class WeightedRandomList<E>
{
	private final class Entry extends WeightedRandom.Item
	{
		private final E value;
		
		private Entry(E value, int weight)
		{
			super(weight);
			
			this.value = value;
		}
	}
	
	private List<Entry> entries = new ArrayList<>();
	private int total = 0;
	
	public WeightedRandomList<E> add(E value, int weight)
	{
		if (weight == 0)
			throw new IllegalArgumentException();
		
		entries.add(new Entry(value, weight));
		total += weight;
		return this;
	}
	
	public WeightedRandomList<E> clear()
	{
		entries.clear();
		total = 0;
		return this;
	}
	
	public boolean has()
	{
		return total > 0;
	}
	
	public E get(Random rand)
	{
		if (total <= 0)
			return null;
		
		return WeightedRandom.getRandomItem(rand, entries, total).value;
	}
}
