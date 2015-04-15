package genesis.metadata;

import genesis.common.GenesisBlocks;
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
