package genesis.metadata;

public enum EnumMenhirPart implements IMetadata
{
	TOP("top"), RECEPTACLE("receptacle"), GLYPH("glyph");
	
	final String name;
	final String unlocalizedName;
	
	EnumMenhirPart(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
	}
	
	EnumMenhirPart(String name)
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