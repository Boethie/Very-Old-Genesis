package genesis.metadata;

import net.minecraft.item.Item;

public enum EnumToolMaterial implements IMetadata
{
	GRANITE("granite"), RHYOLITE("rhyolite"), QUARTZITE("quartzite"), DOLERITE("dolerite"), BROWNFLINT("brownFlint");



	String name;
	String unlocalizedName;

	EnumToolMaterial(String name)
	{
		this(name, name);
	}

	EnumToolMaterial(String name, String unlocalizedName)
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
		return unlocalizedName;
	}
}
