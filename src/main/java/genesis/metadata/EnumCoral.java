package genesis.metadata;


public enum EnumCoral implements IMetadata
{
	FAVOSITES("favosites"), HELIOLITES("heliolites"), HALYSITES("halysites");

	private final String name;
	private final String unlocalizedName;

	EnumCoral(String name)
	{
		this(name, name);
	}

	EnumCoral(String name, String unlocalizedName)
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
