package genesis.util.random.f;

import java.util.Random;

public interface FloatRange
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
	
	public float get(Random rand);
	public float getMin();
	public float getMax();
	
	static class Range implements FloatRange
	{
		private final float min;
		private final float max;
		
		protected Range(float min, float max)
		{
			this.min = min;
			this.max = max;
		}
		
		@Override
		public float get(Random rand)
		{
			return min + rand.nextFloat() * (max - min);
		}
		
		@Override
		public float getMin()
		{
			return min;
		}
		
		@Override
		public float getMax()
		{
			return max;
		}
	}
	
	static class Value implements FloatRange
	{
		private final float value;
		
		protected Value(float value)
		{
			this.value = value;
		}
		
		@Override
		public float get(Random rand)
		{
			return value;
		}
		
		@Override
		public float getMin()
		{
			return value;
		}
		
		@Override
		public float getMax()
		{
			return value;
		}
	}
}
