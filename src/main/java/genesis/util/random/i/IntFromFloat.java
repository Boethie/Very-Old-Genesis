package genesis.util.random.i;

import java.util.Random;

public class IntFromFloat implements RandomIntProvider
{
	private final int min;
	private final float chance;
	
	public IntFromFloat(float value)
	{
		min = (int) value;
		chance = value - min;
	}
	
	@Override
	public int get(Random rand)
	{
		if (rand.nextFloat() <= chance)
			return min + 1;
		return min;
	}
}
