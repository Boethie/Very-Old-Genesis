package genesis.util;

import java.util.Random;

import net.minecraft.util.MathHelper;

public class RandomRange
{
	public int min;
	public int max;
	
	public RandomRange(int minIn, int maxIn)
	{
		min = minIn;
		max = maxIn;
	}
	
	public int getRandomAmount(Random rand)
	{
		return MathHelper.getRandomIntegerInRange(rand, min, max);
	}
}
