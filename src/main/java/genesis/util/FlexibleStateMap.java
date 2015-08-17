package genesis.util;

import java.util.*;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class FlexibleStateMap extends StateMapperBase
{
	protected String prefix = "";
	protected String prefixSeparator = "";
	protected IProperty nameProperty = null;
	protected String postfixSeparator = "";
	protected String postfix = "";
	protected Function<String, String> nameFunction = null;
	protected ArrayList<IProperty> ignoreProperties;
	
	public FlexibleStateMap(IProperty... ignoreProperties)
	{
		this.ignoreProperties = Lists.newArrayList(ignoreProperties);
	}
	
	public FlexibleStateMap setPrefix(String prefix, String separator)
	{
		this.prefixSeparator = separator;
		this.prefix = prefix;
		
		return this;
	}
	
	public FlexibleStateMap setNameProperty(IProperty nameProperty)
	{
		this.nameProperty = nameProperty;
		
		return this;
	}
	
	public FlexibleStateMap setPostfix(String postfix, String separator)
	{
		this.postfixSeparator = separator;
		this.postfix = postfix;
		
		return this;
	}

	public void setNameFunction(Function<String, String> nameFunction)
	{
		this.nameFunction = nameFunction;
	}
	
	public FlexibleStateMap clearIgnoredProperties()
	{
		ignoreProperties.clear();
		
		return this;
	}
	
	public FlexibleStateMap addIgnoredProperties(IProperty... addProperties)
	{
        Collections.addAll(ignoreProperties, addProperties);
		
		return this;
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		ResourceLocation registeredAs = (ResourceLocation) Block.blockRegistry.getNameForObject(state.getBlock());
		String domain = registeredAs.getResourceDomain();
		
		// Set prefix name section.
		String outPrefix = prefix;
		
		String outMain = "";
		Map<IProperty, Comparable<?>> propertyMap = new LinkedHashMap<IProperty, Comparable<?>>(state.getProperties());
		
		// Set main name section.
		if (nameProperty != null)
		{
            outMain = nameProperty.getName(propertyMap.remove(nameProperty));
		}
		
		// Set prefix name section.
		String outPostfix = postfix;
		
		// Remove ignored properties.
		for (IProperty property : ignoreProperties)
		{
			propertyMap.remove(property);
		}
		
		// Begin constructing complete output name, with separators added according to what's a non-empty string.
		String output = outPrefix;
		
		if (!"".equals(outMain))
		{
			if (!"".equals(output))
			{
				output += prefixSeparator;
			}
			
			output += outMain;
		}
		
		if (!"".equals(output))
		{
			output += postfixSeparator;
		}
		
		output += outPostfix;
		
		// Call name function on our output.
		if (nameFunction != null)
		{
			output = nameFunction.apply(output);
		}
		
		if ("".equals(output))
		{
			outMain = registeredAs.getResourcePath();
		}
		
		return new ModelResourceLocation(domain + ":" + output, getPropertyString(propertyMap));
	}
}
