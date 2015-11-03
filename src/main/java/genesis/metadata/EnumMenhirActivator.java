package genesis.metadata;

public enum EnumMenhirActivator implements IMetadata
{
	RUSTED_OCTAEDRITE_FRAGMENT("rusted_octaedrite_fragment", "rustedOctaedriteFragment"),
	ANCIENT_AMBER("ancient_amber", "ancientAmber"),
	FOSSILIZED_EGG("fossilized_egg", "fossilizedEgg"),
	BROKEN_CEREMONIAL_AXE("broken_ceremonial_axe", "brokenCeremonialAxe"),
	BROKEN_SPIRIT_MASK("broken_spirit_mask", "brokenSpiritMask");
	
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