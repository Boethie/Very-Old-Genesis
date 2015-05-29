package genesis.util;

import java.util.Random;

import net.minecraft.util.MathHelper;

public class RandomFloatRange
{
	public float min;
	public float max;
	
	public RandomFloatRange(float minIn, float maxIn)
	{
		min = minIn;
		max = maxIn;
	}
	
	public float getRandom(Random rand)
	{
		return min + rand.nextFloat() * (max - min);
	}
}
