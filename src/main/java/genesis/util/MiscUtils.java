package genesis.util;

import com.google.common.collect.Iterators;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

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
		Collections.addAll(out, values);
		return Collections.unmodifiableSet(out);
	}
}
