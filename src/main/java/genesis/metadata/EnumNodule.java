package genesis.metadata;

import genesis.common.GenesisItems;
import genesis.util.Metadata;
import net.minecraft.item.Item;

public enum EnumNodule implements IMetadata
{
	BROWN_FLINT("brown_flint", "brownFlint"), MARCASITE("marcasite");

	private final String name;
	private final String unlocalizedName;

	EnumNodule(String name)
	{
		this(name, name);
	}

	EnumNodule(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		Metadata.add(this);
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
		return GenesisItems.nodule;
	}
}
