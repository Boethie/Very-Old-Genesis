package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.item.IMetadata;
import genesis.util.Metadata;
import net.minecraft.item.Item;

public enum EnumDung implements IMetadata
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
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

	@Override
	public Item getItem()
	{
		return null;//Item.getItemFromBlock(GenesisBlocks.dung);
	}
}
