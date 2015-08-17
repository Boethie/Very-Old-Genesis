package genesis.metadata;

import genesis.common.GenesisItems;
import net.minecraft.item.Item;

public enum EnumNodule implements IMetadata
{
	BROWN_FLINT("brown_flint", "brownFlint"), BLACK_FLINT("black_flint", "blackFlint"), MARCASITE("marcasite");
	
	public static EnumNodule fromToolMaterial(EnumToolMaterial material)
	{
		switch (material)
		{
		case BROWN_FLINT:
			return BROWN_FLINT;
		case BLACK_FLINT:
			return BLACK_FLINT;
		default:
			return null;
		}
	}
	
	private final String name;
	private final String unlocalizedName;
	
	EnumNodule(String name)
	{
		this(name, name);
	}
	
	EnumNodule(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
}
