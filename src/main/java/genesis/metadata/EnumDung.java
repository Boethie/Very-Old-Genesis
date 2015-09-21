package genesis.metadata;

public enum EnumDung implements IMetadata
{
	SAUROPODA("sauropoda"), THEROPODA("theropoda");

	final String name;
	final String unlocalizedName;

	EnumDung(String name)
	{
		this(name, name);
	}

	EnumDung(String name, String unlocalizedName)
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
