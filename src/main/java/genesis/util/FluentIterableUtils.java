package genesis.util;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

public class FluentIterableUtils
{
	public static <I, K, V> ImmutableMap<K, V> toMap(Iterable<I> inputs, Function<I, Pair<K, V>> func)
	{
		ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
		
		for (I input : inputs)
		{
			Pair<K, V> entry = func.apply(input);
			if (entry != null)
				builder.put(entry);
		}
		
		return builder.build();
	}
}
