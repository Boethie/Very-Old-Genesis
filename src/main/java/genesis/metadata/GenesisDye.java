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
		ImmutableList.Builder<GenesisDye> dyesBuilder = ImmutableList.builder();
		ImmutableMap.Builder<EnumDyeColor, GenesisDye> getterBuilder = ImmutableMap.builder();
		
		for (EnumDyeColor color : EnumDyeColor.values())
		{
			GenesisDye variant = new GenesisDye(color);
			dyesBuilder.add(variant);
			getterBuilder.put(color, variant);
		}
		
		DYES = dyesBuilder.build();
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
	
	@Override
	public int compareTo(GenesisDye o)
	{
		return getColor().compareTo(o.getColor());
	}
}
