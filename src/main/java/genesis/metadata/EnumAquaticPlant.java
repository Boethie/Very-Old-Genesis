package genesis.metadata;

import genesis.common.GenesisBlocks;
import genesis.util.Metadata;
import net.minecraft.item.Item;

public enum EnumAquaticPlant implements IMetaSingle
{

	BANGIOMORPHA("bangiomorpha"), MARPOLIA("marpolia"), MARGERETIA("margaretia"), CHANCELLORIA("chancelloria"), HAZELLA("hazelia"),
	DIAONIELLA("diagoniella"), PIRANIA("pirania"), VAUXIA("vauxia"), WAPKIA("wapkia"), PTERIDINIUM("pteridinium"), DINOMISCHUS("dinomischus"),
	PRIMOCANDELABRUM("primocandelabrum"), CHARNIA("charnia"), CHARNIA_TOP("charnia_top");

	private final String name;
	private final String unlocalizedName;

	private EnumAquaticPlant(String name)
	{
		this(name, name);
	}

	private EnumAquaticPlant(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		Metadata.add(this);
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public String getUnlocalizedName()
	{
		return this.unlocalizedName;
	}

	@Override
	public Item getItem()
	{
		return Item.getItemFromBlock(GenesisBlocks.aquatic_plant);
	}

}
