package genesis.metadata;

import genesis.common.GenesisBlocks;
import genesis.common.GenesisItems;
import net.minecraft.block.Block;
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
