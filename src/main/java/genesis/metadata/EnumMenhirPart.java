package genesis.metadata;

public enum EnumMenhirPart implements IMetadata
{
	GLYPH("glyph", false), RECEPTACLE("receptacle", false), TOP("top", true);
	
	final String name;
	final String unlocalizedName;
	final boolean canStack;
	
	EnumMenhirPart(String name, String unlocalizedName, boolean canStack)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.canStack = canStack;
	}
	
	EnumMenhirPart(String name, boolean canStack)
	{
		this(name, name, canStack);
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
	
	public boolean canStack()
	{
		return canStack;
	}
}