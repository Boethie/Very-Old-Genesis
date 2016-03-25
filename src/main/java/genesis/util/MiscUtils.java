package genesis.util;

import com.google.common.collect.*;

public class MiscUtils
{
	@SafeVarargs
	public static <T> Iterable<T> iterable(final T... array)
	{
		return () -> Iterators.forArray(array);
	}
	
	@SafeVarargs
	public static <T> FluentIterable<T> fluentIterable(final T... array)
	{
		return FluentIterable.from(iterable(array));
	}
}
