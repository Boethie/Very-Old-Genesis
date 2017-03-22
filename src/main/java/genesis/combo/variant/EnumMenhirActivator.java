package genesis.combo.variant;

public enum EnumMenhirActivator implements IMetadata<EnumMenhirActivator>
{
	ANCIENT_AMBER("ancient_amber", "ancientAmber", true),
	FOSSILIZED_EGG("fossilized_egg", "fossilizedEgg", true),
	BROKEN_SPIRIT_MASK("broken_spirit_mask", "brokenSpiritMask", true),
	PRIMITIVE_DUST("primitive_dust", "primitiveDust", true);
	
	final String name;
	final String unlocalizedName;
	final boolean forOverworld;
	
	EnumMenhirActivator(String name, String unlocalizedName, boolean forOverworld)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.forOverworld = forOverworld;
	}
	
	EnumMenhirActivator(String name, boolean forOverworld)
	{
		this(name, name, forOverworld);
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
	
	public boolean isForOverworld()
	{
		return forOverworld;
	}
}
