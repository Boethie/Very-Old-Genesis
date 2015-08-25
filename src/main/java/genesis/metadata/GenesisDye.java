package genesis.metadata;

import java.util.*;

import net.minecraft.item.*;

public class GenesisDye implements IMetadata, Comparable<GenesisDye>
{
	protected static final List<GenesisDye> DYES = new ArrayList<GenesisDye>();
	protected static final LinkedHashMap<EnumDyeColor, GenesisDye> GETTER_MAP = new LinkedHashMap<EnumDyeColor, GenesisDye>();
	
	static
	{
		for (EnumDyeColor color : EnumDyeColor.values())
		{
			new GenesisDye(color);
		}
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
		GETTER_MAP.put(color, this);
		DYES.add(this);
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
