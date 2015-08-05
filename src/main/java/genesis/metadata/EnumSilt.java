package genesis.metadata;

public enum EnumSilt implements IMetadata
{
	SILT("", "", "default"), RED_SILT("red");
	
	protected final String name;
	protected final String unlocalizedName;
	protected final String toString;
	
	private EnumSilt(String name, String unlocalizedName, String toString)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.toString = toString;
	}
	
	private EnumSilt(String name, String unlocalizedName)
	{
		this(name, unlocalizedName, name);
	}
	
	private EnumSilt(String name)
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
	
	@Override
	public String toString()
	{
		return toString;
	}
}
