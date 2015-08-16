package genesis.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.item.EnumDyeColor;

public class EnumDye implements IMetadata, Comparable<EnumDye>
{
	protected static final List<EnumDye> DYES = new ArrayList<EnumDye>();
	protected static final LinkedHashMap<EnumDyeColor, EnumDye> GETTER_MAP = new LinkedHashMap<EnumDyeColor, EnumDye>();
	
	static
	{
		for (EnumDyeColor color : EnumDyeColor.values())
		{
			new EnumDye(color);
		}
	}
	
	public static EnumDye get(EnumDyeColor color)
	{
		return GETTER_MAP.get(color);
	}
	
	public static List<EnumDye> valueList()
	{
		return DYES;
	}
	
	protected final EnumDyeColor color;
	
	protected EnumDye(EnumDyeColor color)
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
	public int compareTo(EnumDye o)
	{
		return getColor().compareTo(o.getColor());
	}
}
