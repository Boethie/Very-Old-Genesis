package genesis.combo.variant;

import net.minecraft.item.EnumDyeColor;

public enum EnumPowder implements IMetadata<EnumPowder>
{
	LIMESTONE("limestone", EnumDyeColor.WHITE, null),
	HEMATITE("hematite", EnumDyeColor.RED, EnumOre.HEMATITE),
	MANGANESE("manganese", EnumDyeColor.BLACK, EnumOre.MANGANESE),
	MALACHITE("malachite", EnumDyeColor.GREEN, EnumOre.MALACHITE),
	AZURITE("azurite", EnumDyeColor.BLUE, EnumOre.AZURITE);
	
	final String name;
	final String unlocalizedName;
	final EnumDyeColor color;
	final EnumOre craftOreDrop;
	
	EnumPowder(String name, String unlocalizedName, EnumDyeColor color, EnumOre craftOreDrop)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.color = color;
		this.craftOreDrop = craftOreDrop;
	}
	
	EnumPowder(String name, EnumDyeColor color, EnumOre craftOreDrop)
	{
		this(name, name, color, craftOreDrop);
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
	
	public EnumOre getCraftingOreDrop()
	{
		return craftOreDrop;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}
