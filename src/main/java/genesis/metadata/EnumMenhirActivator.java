package genesis.metadata;

public enum EnumMenhirActivator implements IMetadata
{
	RUSTED_OCTAEDRITE_FLAKE("rusted_octaedrite_flake", "rustedOctaedriteFlake"),
	BROKEN_CEREMONIAL_AXE("broken_ceremonial_axe", "brokenCeremonialAxe"),
	BROKEN_SPIRIT_MASK("broken_spirit_mask", "brokenSpiritMask"),
	TREPANNED_HOMONID_SKULL("trepanned_homonid_skull", "trepannedHomonidSkull"),
	ANCIENT_AMBER("ancient_amber", "ancientAmber"),
	CARBONIZED_CONE("carbonized_cone", "carbonizedCone"),
	FOSSILIZED_EGG("fossilized_egg", "fossilizedEgg");
	
	final String name;
	final String unlocalizedName;
	
	EnumMenhirActivator(String name, String unlocalizedName)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
	}
	
	EnumMenhirActivator(String name)
	{
		this(name, name);
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