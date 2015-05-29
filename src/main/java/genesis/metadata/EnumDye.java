package genesis.metadata;

import java.util.*;

import net.minecraft.item.*;

public class EnumDye implements IMetadata
{
	protected static final List<EnumDye> DYES = new ArrayList();
	protected static final LinkedHashMap<EnumDyeColor, EnumDye> GETTER_MAP = new LinkedHashMap();
	
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
}
