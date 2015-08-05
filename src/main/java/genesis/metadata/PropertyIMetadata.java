package genesis.metadata;

import java.util.*;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.properties.*;

public class PropertyIMetadata<T extends IMetadata> extends PropertyHelper
{
	protected final ImmutableSet<T> values;
	
	public PropertyIMetadata(String name, List<T> values)
	{
		super(name, IMetadata.class);
		
		this.values = ImmutableSet.copyOf(values);
	}
	
	@Override
	public Collection getAllowedValues()
	{
		return values;
	}
	
	@Override
	public String getName(Comparable value)
	{
		return ((IMetadata) value).getName();
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
				Iterator<Comparable> ourValIter = getAllowedValues().iterator();
				Iterator<Comparable> otherValIter = propIMeta.getAllowedValues().iterator();
				
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
