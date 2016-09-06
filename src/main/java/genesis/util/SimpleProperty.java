package genesis.util;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;
import java.util.List;

public abstract class SimpleProperty<T extends Comparable<T>> extends PropertyHelper<T>
{
	private final ImmutableList<T> values;
	private final ImmutableMap<String, T> parseMap;

	protected SimpleProperty(String name, List<T> values, Class<T> valueClass)
	{
		super(name, valueClass);

		this.values = ImmutableList.copyOf(values);
		parseMap = values.stream().collect(StreamUtils.toImmMap(this::getName, (v) -> v));
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
