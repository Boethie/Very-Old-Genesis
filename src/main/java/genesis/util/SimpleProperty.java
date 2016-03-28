package genesis.util;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.properties.PropertyHelper;

public abstract class SimpleProperty<T extends Comparable<T>> extends PropertyHelper<T>
{
	private final ImmutableMap<String, T> parseMap;
	
	protected SimpleProperty(String name, Class<T> valueClass)
	{
		super(name, valueClass);
		
		parseMap = FluentIterableUtils.toMap(getAllowedValues(), (v) -> Pair.of(getName(v), v));
	}
	
	@Override
	public Optional<T> parseValue(String value)
	{
		if (parseMap.containsKey(value))
			return Optional.of(parseMap.get(value));
		
		return Optional.absent();
	}
}
