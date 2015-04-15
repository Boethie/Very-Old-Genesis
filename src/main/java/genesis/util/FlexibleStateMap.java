package genesis.util;

import java.util.*;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class FlexibleStateMap extends StateMapperBase
{
	protected String prefix = "";
	protected IProperty nameProperty = null;
	protected String postfix = "";
	protected Function<String, String> nameFunction = null;
	protected ArrayList<IProperty> ignoreProperties;
	
	public FlexibleStateMap(IProperty... ignoreProperties)
	{
		this.ignoreProperties = Lists.newArrayList(ignoreProperties);
	}
	
	public FlexibleStateMap setPrefix(String prefix)
	{
		this.prefix = prefix;
		
		return this;
	}
	
	public FlexibleStateMap setPostfix(String postfix)
	{
		this.postfix = postfix;
		
		return this;
	}
	
	public FlexibleStateMap setNameProperty(IProperty nameProperty)
	{
		this.nameProperty = nameProperty;
		
		return this;
	}

	public void setNameFunction(Function<String, String> nameFunction)
	{
		this.nameFunction = nameFunction;
	}
	
	public FlexibleStateMap addIgnoredProperties(IProperty... addProperties)
	{
        Collections.addAll(ignoreProperties, addProperties);
		
		return this;
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		String output = Constants.ASSETS + prefix;
		
		Map<IProperty, Comparable> propertyMap = Maps.newLinkedHashMap(state.getProperties());
		
		if (nameProperty != null)
		{
            output += nameProperty.getName(propertyMap.remove(nameProperty));
		}
		
		output += postfix;
		
		for (IProperty property : ignoreProperties)
		{
			propertyMap.remove(property);
		}
		
		if (nameFunction != null)
		{
			output = nameFunction.apply(output);
		}
		
		return new ModelResourceLocation(output, getPropertyString(propertyMap));
	}
}
