package genesis.util;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import net.minecraft.block.properties.PropertyHelper;

public abstract class SimpleProperty<T extends Comparable<T>> extends PropertyHelper<T>
{
	private final ImmutableList<T> values;
	private final ImmutableMap<String, T> parseMap;
	
	protected SimpleProperty(String name, List<T> values, Class<T> valueClass)
	{
		super(name, valueClass);
		
		this.values = ImmutableList.copyOf(values);
		parseMap = values.stream().collect(StreamUtils.toImmMap((v) -> getName(v), (v) -> v));
	}
	
	@Override
	public final Collection<T> getAllowedValues()
	{
		return values;
	}
	
	@Override
	public final Optional<T> parseValue(String value)
	{
		if (parseMap.containsKey(value))
			return Optional.of(parseMap.get(value));
		
		return Optional.absent();
	}
}
