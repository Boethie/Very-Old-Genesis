package genesis.combo.variant;

import java.util.*;

import com.google.common.collect.ImmutableList;

import genesis.util.SimpleProperty;

public class PropertyIMetadata<T extends IMetadata<T>> extends SimpleProperty<T>
{
	protected final ImmutableList<T> values;
	
	public PropertyIMetadata(String name, List<T> values, Class<T> clazz)
	{
		super(name, clazz);
		
		this.values = ImmutableList.copyOf(values);
	}
	
	@Override
	public Collection<T> getAllowedValues()
	{
		return values;
	}
	
	@Override
	public String getName(T value)
	{
		return value.getName();
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		
		if (other != null && getClass() == other.getClass())
		{
			PropertyIMetadata<?> propIMeta = (PropertyIMetadata<?>) other;
			
			if (getName().equals(propIMeta.getName()))
			{
				Iterator<T> ourValIter = getAllowedValues().iterator();
				Iterator<?> otherValIter = propIMeta.getAllowedValues().iterator();
				
				while (ourValIter.hasNext() && otherValIter.hasNext())
				{
					if (ourValIter.next() != otherValIter.next())
					{
						return false;
					}
				}
				
				if (!ourValIter.hasNext() && !otherValIter.hasNext())
				{
					return true;
				}
			}
		}
		
		return false;
	}
}
