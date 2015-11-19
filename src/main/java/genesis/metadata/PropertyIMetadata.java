package genesis.metadata;

import java.util.*;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.properties.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PropertyIMetadata<T extends IMetadata> extends PropertyHelper
{
	protected final List<? extends T> values;
	
	public PropertyIMetadata(String name, List<? extends T> values)
	{
		super(name, IMetadata.class);
		
		this.values = ImmutableList.copyOf(values);
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
