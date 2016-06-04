package genesis.util.random.d;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public interface DoubleRange
{
	static DoubleRange create(double value)
	{
		return new Value(value);
	}
	
	static DoubleRange create(double min, double max)
	{
		if (min == max)
		{
			return create(min);
		}
		
		return new Range(min, max);
	}
	
	double get(Random rand);
	double getMin();
	double getMax();
	
	class Range implements DoubleRange
	{
		public final double min;
		public final double max;
		
		protected Range(double minIn, double maxIn)
		{
			min = minIn;
			max = maxIn;
		}
		
		@Override
		public double get(Random rand)
		{
			return MathHelper.getRandomDoubleInRange(rand, min, max);
		}
		
		@Override
		public double getMin()
		{
			return min;
		}
		
		@Override
		public double getMax()
		{
			return max;
		}
	}
	
	class Value implements DoubleRange
	{
		public final double value;
		
		protected Value(double value)
		{
			this.value = value;
		}
		
		@Override
		public double get(Random rand)
		{
			return value;
		}
		
		@Override
		public double getMin()
		{
			return value;
		}
		
		@Override
		public double getMax()
		{
			return value;
		}
	}
}
