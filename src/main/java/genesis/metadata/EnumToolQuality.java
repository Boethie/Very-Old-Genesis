package genesis.metadata;

import genesis.util.Constants;

public enum EnumToolQuality implements IMetadata
{
	NONE(null), CHIPPED("chipped"), POLISHED("polished"), SHARPENED("sharpened");

	String name;
	String unlocalizedName;

	EnumToolQuality(String name)
	{
		this(name, name);
	}

	EnumToolQuality(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getUnlocalizedName()
	{
		return Constants.PREFIX + "tool.quality." + unlocalizedName;
	}
}
