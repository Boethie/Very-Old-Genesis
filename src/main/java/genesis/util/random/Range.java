package genesis.util.random;

import java.util.Random;

public interface Range<T>
{
	public T get(Random rand);
	public T getMin();
	public T getMax();
}
