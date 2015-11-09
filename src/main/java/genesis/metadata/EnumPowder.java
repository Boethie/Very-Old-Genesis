package genesis.metadata;

public enum EnumPowder implements IMetadata
{
	LIMESTONE("limestone"), HEMATITE("hematite"), MANGANESE("manganese"), MALACHITE("malachite"), AZURITE("azurite");
	
	final String name;
	final String unlocalizedName;
	
	EnumPowder(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
	}
	
	EnumPowder(String name)
	{
		this(name, name);
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
