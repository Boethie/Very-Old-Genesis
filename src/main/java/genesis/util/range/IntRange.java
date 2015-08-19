package genesis.util.range;

import java.util.Random;

import net.minecraft.util.MathHelper;

public abstract class IntRange implements Range<Integer>
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
	
	protected static class Range extends IntRange
	{
		private final int min;
		private final int max;
		
		protected Range(int min, int max)
		{
			this.min = min;
			this.max = max;
		}
		
		@Override
		public Integer get(Random rand)
		{
			return MathHelper.getRandomIntegerInRange(rand, min, max);
		}
		
		@Override
		public Integer getMin()
		{
			return min;
		}
		
		@Override
		public Integer getMax()
		{
			return max;
		}
	}
	
	protected static class Value extends IntRange
	{
		private final int value;
		
		protected Value(int value)
		{
			this.value = value;
		}
		
		@Override
		public Integer get(Random rand)
		{
			return value;
		}
		
		@Override
		public Integer getMin()
		{
			return value;
		}
		
		@Override
		public Integer getMax()
		{
			return value;
		}
	}
}
