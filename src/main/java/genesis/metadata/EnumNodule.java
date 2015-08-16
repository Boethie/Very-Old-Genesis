package genesis.metadata;


public enum EnumNodule implements IMetadata
{
	BROWN_FLINT("brown_flint", "brownFlint"), BLACK_FLINT ("black_flint", "blackFlint"), MARCASITE("marcasite");

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
