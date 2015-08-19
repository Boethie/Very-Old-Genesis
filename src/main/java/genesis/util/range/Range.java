package genesis.util.range;

import java.util.Random;

public interface Range<T>
{
	public T get(Random rand);
	public T getMin();
	public T getMax();
}
