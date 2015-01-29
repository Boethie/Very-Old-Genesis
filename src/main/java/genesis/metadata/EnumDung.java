package genesis.metadata;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import genesis.util.Metadata;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public enum EnumDung implements IMetaMulti
{
	BRACHIOSAURUS("brachiosaurus"), TYRANNOSAURUS("tyrannosaurus");

	private final String name;
	private final String unlocalizedName;

	EnumDung(String name)
	{
		this(name, name);
	}

	EnumDung(String name, String unlocalizedName)
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
	public String getName(String type)
	{
		if ("block".equals(type))
		{
			return "dung_block_" + getName();
		}
		else
		{
			return getName();
		}
	}

	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public Item getItem(String type)
	{
		if ("block".equals(type))
		{
			return Item.getItemFromBlock(GenesisBlocks.dung_block);
		}
		else
		{
			return GenesisItems.dung;
		}
	}
}
