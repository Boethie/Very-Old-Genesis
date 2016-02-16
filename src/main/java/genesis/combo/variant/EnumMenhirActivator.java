package genesis.combo.variant;

public enum EnumMenhirActivator implements IMetadata<EnumMenhirActivator>
{
	ANCIENT_AMBER("ancient_amber", "ancientAmber", true),
	FOSSILIZED_EGG("fossilized_egg", "fossilizedEgg", true),
	BROKEN_SPIRIT_MASK("broken_spirit_mask", "brokenSpiritMask", true),
	RUSTED_OCTAEDRITE_FLAKE("rusted_octaedrite_flake", "rustedOctaedriteFlake", true),
	SHIMMERING_TREE_STAR("shimmering_tree_star", "shimmeringTreeStar", false),
	SHADY_DRAGON_BONE("shady_dragon_bone", "shadyDragonBone", false),
	LEGENDARY_SPECTRUM_EYE("legendary_spectrum_eye", "legendarySpectrumEye", false),
	PRIMITIVE_DUST("primitive_dust", "primitiveDust", false);
	
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
