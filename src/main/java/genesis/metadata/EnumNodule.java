package genesis.metadata;

public enum EnumNodule implements IMetadata<EnumNodule>
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
	
	final String name;
	final String unlocalizedName;
	
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
