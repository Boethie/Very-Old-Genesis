package genesis.item;

import genesis.common.GenesisItems;
import genesis.util.Metadata;
import net.minecraft.item.Item;

public enum EnumPebble implements IMetadata
{
	GRANITE("granite"), RHYOLITE("rhyolite"), DOLERITE("dolerite"), QUARTZITE("quartzite"), BROWN_FLINT("brown_flint", "brownFlint");

	private final String name;
	private final String unlocalizedName;

	EnumPebble(String name)
	{
		this(name, name);
	}

	EnumPebble(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		Metadata.add(getClass(), this);
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
	public Item getItem()
	{
		return GenesisItems.pebble;
	}
}
