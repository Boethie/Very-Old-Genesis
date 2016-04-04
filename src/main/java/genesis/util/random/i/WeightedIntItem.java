package genesis.util.random.i;

import java.util.Random;

import net.minecraft.util.WeightedRandom;

public class WeightedIntItem extends WeightedRandom.Item implements RandomIntProvider
{
	public static WeightedIntItem of(int weight, RandomIntProvider provider)
	{
		return new WeightedIntItem(weight, provider);
	}
	
	public static WeightedIntItem of(int weight, int min, int max)
	{
		return new WeightedIntItem(weight, IntRange.create(min, max));
	}
	
	public static WeightedIntItem of(int weight, int value)
	{
		return new WeightedIntItem(weight, IntRange.create(value));
	}
	
	private final RandomIntProvider provider;
	
	public WeightedIntItem(int weight, RandomIntProvider provider)
	{
		super(weight);
		this.provider = provider;
	}
	
	@Override
	public int get(Random rand)
	{
		return provider.get(rand);
	}
}
