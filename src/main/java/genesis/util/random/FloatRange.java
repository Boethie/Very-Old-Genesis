package genesis.util.random;

import java.util.Random;

import net.minecraft.util.MathHelper;

public abstract class FloatRange implements Range<Float>
{
	public static FloatRange create(float value)
	{
		return new Value(value);
	}
	
	public static FloatRange create(float min, float max)
	{
		if (min == max)
		{
			return create(min);
		}
		
		return new Range(min, max);
	}
	
	protected static class Range extends FloatRange
	{
		private final float min;
		private final float max;
		
		protected Range(float min, float max)
		{
			this.min = min;
			this.max = max;
		}
		
		@Override
		public Float get(Random rand)
		{
			return min + rand.nextFloat() * (max - min);
		}
		
		@Override
		public Float getMin()
		{
			return min;
		}
		
		@Override
		public Float getMax()
		{
			return max;
		}
	}
	
	protected static class Value extends FloatRange
	{
		private final float value;
		
		protected Value(float value)
		{
			this.value = value;
		}
		
		@Override
		public Float get(Random rand)
		{
			return value;
		}
		
		@Override
		public Float getMin()
		{
			return value;
		}
		
		@Override
		public Float getMax()
		{
			return value;
		}
	}
}
