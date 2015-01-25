package genesis.metadata;

import genesis.common.GenesisBlocks;
import genesis.util.Metadata;
import net.minecraft.item.Item;

public enum EnumCoral implements IMetaSingle
{
	FAVOSITES("favosites"), HELIOLITES("heliolites"), HALYSITES("halysites");

	private final String name;
	private final String unlocalizedName;

	EnumCoral(String name)
	{
		this(name, name);
	}

	EnumCoral(String name, String unlocalizedName)
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
		return Item.getItemFromBlock(GenesisBlocks.coral);
	}
}
