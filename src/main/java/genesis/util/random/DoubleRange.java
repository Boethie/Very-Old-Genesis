package genesis.util.random;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public abstract class DoubleRange implements Range<Double>
{
	public static DoubleRange create(double value)
	{
		return new Value(value);
	}
	
	public static DoubleRange create(double min, double max)
	{
		if (min == max)
		{
			return create(min);
		}
		
		return new Range(min, max);
	}
	
	protected static class Range extends DoubleRange
	{
		public final double min;
		public final double max;
		
		protected Range(double minIn, double maxIn)
		{
			min = minIn;
			max = maxIn;
		}
		
		@Override
		public Double get(Random rand)
		{
			return MathHelper.getRandomDoubleInRange(rand, min, max);
		}
		
		@Override
		public Double getMin()
		{
			return min;
		}
		
		@Override
		public Double getMax()
		{
			return max;
		}
	}
	
	protected static class Value extends DoubleRange
	{
		public final double value;
		
		protected Value(double value)
		{
			this.value = value;
		}
		
		@Override
		public Double get(Random rand)
		{
			return value;
		}
		
		@Override
		public Double getMin()
		{
			return value;
		}
		
		@Override
		public Double getMax()
		{
			return value;
		}
	}
}
