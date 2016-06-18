package genesis.util;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.*;

public final class MiscUtils
{
	@SafeVarargs
	public static <T> Iterable<T> iterable(final T... array)
	{
		return () -> Iterators.forArray(array);
	}
	
	@SafeVarargs
	public static <T extends Enum<T>> Set<T> unmodSet(T... values)
	{
		Set<T> out = EnumSet.noneOf(ReflectionUtils.getClass(values));
		for (T value : values)
			out.add(value);
		return Collections.unmodifiableSet(out);
	}
}
