package genesis.util.random.i;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public abstract class IntRange implements RandomIntProvider
{
	public static IntRange create(int value)
	{
		return new Value(value);
	}
	
	public static IntRange create(int min, int max)
	{
		if (min == max)
		{
			return create(min);
		}
		
		return new Range(min, max);
	}
	
	public abstract int getMin();
	public abstract int getMax();
	
	private static class Range extends IntRange
	{
		private final int min;
		private final int max;
		
		protected Range(int min, int max)
		{
			this.min = min;
			this.max = max;
		}
		
		@Override
		public int get(Random rand)
		{
			return MathHelper.getRandomIntegerInRange(rand, min, max);
		}
		
		@Override
		public int getMin()
		{
			return min;
		}
		
		@Override
		public int getMax()
		{
			return max;
		}
	}
	
	private static class Value extends IntRange
	{
		public final int value;
		
		protected Value(int value)
		{
			this.value = value;
		}
		
		@Override
		public int get(Random rand)
		{
			return value;
		}
		
		@Override
		public int getMin()
		{
			return value;
		}
		
		@Override
		public int getMax()
		{
			return value;
		}
	}
}
