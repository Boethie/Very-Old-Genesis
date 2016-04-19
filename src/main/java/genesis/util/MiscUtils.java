package genesis.util;

import com.google.common.collect.*;

public class MiscUtils
{
	@SafeVarargs
	public static <T> FluentIterable<T> iterable(final T... array)
	{
		return FluentIterable.from(() -> Iterators.forArray(array));
	}
	
	public static FluentIterable<Integer> range(int min, int max)
	{
		return FluentIterable.from(() -> new SimpleIterator<Integer>()
		{
			@Override
			protected Integer computeNext()
			{
				if (getCurrent() == null)
					return min;
				if (getCurrent() >= max)
					return null;
				return getCurrent() + 1;
			}
		});
	}
}
