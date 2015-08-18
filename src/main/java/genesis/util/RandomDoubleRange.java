package genesis.util;

import java.util.Random;

import net.minecraft.util.MathHelper;

public class RandomDoubleRange
{
	public final double min;
	public final double max;
	
	public RandomDoubleRange(double minIn, double maxIn)
	{
		min = minIn;
		max = maxIn;
	}
	
	public RandomDoubleRange(double value)
	{
		this(value, value);
	}

	public double getRandom(Random rand)
	{
		return MathHelper.getRandomDoubleInRange(rand, min, max);
	}
}
