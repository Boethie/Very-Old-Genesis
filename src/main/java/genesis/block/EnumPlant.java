package genesis.block;

import genesis.common.GenesisBlocks;
import genesis.item.IMetadata;
import genesis.util.Metadata;
import net.minecraft.item.Item;

public enum EnumPlant implements IMetadata
{
	COOKSONIA("cooksonia"), BARAGWANATHIA("baragwanathia"), SCIADOPHYTON("sciadophyton"), PSILOPHYTON("psilophyton"), NOTHIA("nothia"), RHYNIA("rhynia"), ARCHAEAMPHORA("archaeamphora"), MABELIA("mabelia");

	private final String name;
	private final String unlocalizedName;

	EnumPlant(String name)
	{
		this(name, name);
	}

	EnumPlant(String name, String unlocalizedName)
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
		return Item.getItemFromBlock(GenesisBlocks.plant);
	}
}
