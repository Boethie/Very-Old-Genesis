package genesis.metadata;

public enum EnumMenhirActivator implements IMetadata<EnumMenhirActivator>
{
	ANCIENT_AMBER("ancient_amber", "ancientAmber", true),
	FOSSILIZED_EGG("fossilized_egg", "fossilizedEgg", true),
	BROKEN_SPIRIT_MASK("broken_spirit_mask", "brokenSpiritMask", true),
	RUSTED_OCTAEDRITE_FLAKE("rusted_octaedrite_flake", "rustedOctaedriteFlake", true),
	SHIMMERING_TREE_STAR("shimmering_tree_star", "shimmeringTreeStar", false),
	SHADY_DRAGON_BONE("shady_dragon_bone", "shadyDragonBone", true),
	LEGENDARY_SPECTRUM_EYE("legendary_spectrum_eye", "legendarySpectrumEye", false),
	PRIMITIVE_DUST("primitive_dust", "primitiveDust", false);
	
	final String name;
	final String unlocalizedName;
	final boolean isAncient;
	
	EnumMenhirActivator(String name, String unlocalizedName, boolean isAncient)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.isAncient = isAncient;
	}
	
	EnumMenhirActivator(String name, boolean isAncient)
	{
		this(name, name, isAncient);
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
	
	public boolean isAncient()
	{
		return isAncient;
	}
}