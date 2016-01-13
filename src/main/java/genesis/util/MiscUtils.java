package genesis.util;

import java.util.*;

import com.google.common.collect.*;

public class MiscUtils
{
	@SafeVarargs
	public static <T> Iterable<T> iterable(final T... array)
	{
		return new Iterable<T>()
		{
			@Override
			public Iterator<T> iterator()
			{
				return Iterators.forArray(array);
			}
		};
	}
}
