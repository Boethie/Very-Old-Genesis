package genesis.util;

import java.util.Random;

import net.minecraft.util.MathHelper;

public class RandomIntRange
{
	public int min;
	public int max;
	
	public RandomIntRange(int minIn, int maxIn)
	{
		min = minIn;
		max = maxIn;
	}

	public RandomIntRange(int value)
	{
		this(value, value);
	}
	
	public int getRandom(Random rand)
	{
		return MathHelper.getRandomIntegerInRange(rand, min, max);
	}
}
