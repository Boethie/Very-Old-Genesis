package genesis.util;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.properties.PropertyHelper;

public abstract class SimpleProperty<T extends Comparable<T>> extends PropertyHelper<T>
{
	private final ImmutableList<T> values;
	private final ImmutableMap<String, T> parseMap;
	
	protected SimpleProperty(String name, List<T> values, Class<T> valueClass)
	{
		super(name, valueClass);
		
		this.values = ImmutableList.copyOf(values);
		parseMap = FluentIterableUtils.toMap(values, (v) -> Pair.of(getName(v), v));
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
