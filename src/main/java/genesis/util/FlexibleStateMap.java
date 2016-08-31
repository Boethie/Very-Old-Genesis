package genesis.util;

import java.util.*;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class FlexibleStateMap extends StateMapperBase
{
	protected String prefix = "";
	protected String prefixSeparator = "";
	protected IProperty<?> nameProperty = null;
	protected String postfixSeparator = "";
	protected String postfix = "";
	protected Function<String, String> nameFunction = null;
	protected List<IProperty<?>> ignoreProperties;
	
	public FlexibleStateMap(IProperty<?>... ignoreProperties)
	{
		this.ignoreProperties = Lists.newArrayList(ignoreProperties);
	}
	
	public FlexibleStateMap setPrefix(String prefix, String separator)
	{
		this.prefixSeparator = separator;
		this.prefix = prefix;
		
		return this;
	}
	
	public FlexibleStateMap setNameProperty(IProperty<?> nameProperty)
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
	
	public FlexibleStateMap addIgnoredProperties(IProperty<?>... addProperties)
	{
		Collections.addAll(ignoreProperties, addProperties);
		
		return this;
	}
	
	private static <T extends Comparable<T>> String getName(IProperty<T> property, T value)
	{
		return property.getName(value);
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> String getNameUnsafe(IProperty<?> property, Comparable<?> value)
	{
		return getName((IProperty<T>) property, (T) value);
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		ResourceLocation registeredAs = Block.REGISTRY.getNameForObject(state.getBlock());
		String domain = registeredAs.getResourceDomain();
		
		// Set prefix name section.
		String outPrefix = prefix;
		
		String outMain = "";
		Map<IProperty<?>, Comparable<?>> propertyMap = new LinkedHashMap<>(state.getProperties());
		
		// Set main name section.
		if (nameProperty != null)
		{
			outMain = getNameUnsafe(nameProperty, propertyMap.remove(nameProperty));
		}
		
		// Set prefix name section.
		String outPostfix = postfix;
		
		// Remove ignored properties.
		propertyMap.keySet().removeAll(ignoreProperties);
		
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
