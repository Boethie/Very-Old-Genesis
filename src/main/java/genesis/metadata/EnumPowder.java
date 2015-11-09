package genesis.metadata;

import net.minecraft.item.EnumDyeColor;

public enum EnumPowder implements IMetadata
{
	LIMESTONE("limestone", EnumDyeColor.WHITE),
	HEMATITE("hematite", EnumDyeColor.RED),
	MANGANESE("manganese", EnumDyeColor.BLACK),
	MALACHITE("malachite", EnumDyeColor.GREEN),
	AZURITE("azurite", EnumDyeColor.BLUE);
	
	final String name;
	final String unlocalizedName;
	final EnumDyeColor color;
	
	EnumPowder(String name, String unlocalizedName, EnumDyeColor color)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.color = color;
	}
	
	EnumPowder(String name, EnumDyeColor color)
	{
		this(name, name, color);
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
	
	public EnumDyeColor getColor()
	{
		return color;
	}
}
