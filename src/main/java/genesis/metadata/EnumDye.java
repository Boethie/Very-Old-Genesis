package genesis.metadata;

public enum EnumDye implements IMetadata
{
	CALAMITES_YELLOW("calamites_yellow", "calamitesYellow"), ODONTOPTERIS_LIME("odontopteris_lime", "odontopterisLime");

	private final String name;
	private final String unlocalizedName;

	EnumDye(String name)
	{
		this(name, name);
	}

	EnumDye(String name, String unlocalizedName)
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
