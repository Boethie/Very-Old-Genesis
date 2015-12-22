package genesis.util.random;

import java.util.Random;

public interface Range<T>
{
	T get(Random rand);
	T getMin();
	T getMax();
}
