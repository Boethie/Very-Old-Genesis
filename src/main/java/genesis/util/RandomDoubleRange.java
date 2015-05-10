package genesis.util;

import java.util.Random;

import net.minecraft.util.MathHelper;

public class RandomDoubleRange
{
	public double min;
	public double max;
	
	public RandomDoubleRange(double minIn, double maxIn)
	{
		min = minIn;
		max = maxIn;
	}
	
	public double getRandom(Random rand)
	{
		return MathHelper.getRandomDoubleInRange(rand, min, max);
	}
}
