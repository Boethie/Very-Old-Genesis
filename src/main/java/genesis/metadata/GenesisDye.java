package genesis.metadata;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.item.*;

public class GenesisDye implements IMetadata, Comparable<GenesisDye>
{
	protected static final List<GenesisDye> DYES;
	protected static final Map<EnumDyeColor, GenesisDye> GETTER_MAP;
	
	static
	{
		List<GenesisDye> dyes = Lists.newArrayList();
		ImmutableMap.Builder<EnumDyeColor, GenesisDye> getterBuilder = ImmutableMap.builder();
		
		for (EnumDyeColor color : EnumDyeColor.values())
		{
			GenesisDye variant = new GenesisDye(color);
			dyes.add(variant);
			getterBuilder.put(color, variant);
		}
		
		dyes.sort(null);
		DYES = ImmutableList.copyOf(dyes);
		GETTER_MAP = getterBuilder.build();
	}
	
	public static GenesisDye get(EnumDyeColor color)
	{
		return GETTER_MAP.get(color);
	}
	
	public static List<GenesisDye> valueList()
	{
		return DYES;
	}
	
	protected final EnumDyeColor color;
	
	protected GenesisDye(EnumDyeColor color)
	{
		this.color = color;
	}
	
	public EnumDyeColor getColor()
	{
		return color;
	}
	
	@Override
	public String getName()
	{
		return color.getName();
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return color.getUnlocalizedName();
	}
	
	public String getOreDictName()
	{
		switch (color)
		{
		case SILVER:
			return "dyeLightGray";
		default:
			String out = "dye_" + getName();
			int index;
			
			while ((index = out.indexOf('_')) != -1)
			{	// Convert underscores to camelCase
				out = out.substring(0, index) + out.substring(++index, ++index).toUpperCase() + out.substring(index, out.length());
			}
			
			return out;
		}
	}
	
	@Override
	public int compareTo(GenesisDye o)
	{
		return Integer.compare(getColor().getDyeDamage(), o.getColor().getDyeDamage());
	}
	
	@Override
	public String toString()
	{
		return getColor().toString();
	}
}
