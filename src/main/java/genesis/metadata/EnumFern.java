package genesis.metadata;


public enum EnumFern implements IMetadata
{
	ZYGOPTERIS("zygopteris"), PHLEBOPTERIS("phlebopteris"), RUFFORDIA("ruffordia"), ASTRALOPTERIS("astralopteris"), MATONIDIUM("matonidium");

	private final String name;
	private final String unlocalizedName;

	EnumFern(String name)
	{
		this(name, name);
	}

	EnumFern(String name, String unlocalizedName)
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
