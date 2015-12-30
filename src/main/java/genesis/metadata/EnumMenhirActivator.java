package genesis.metadata;

public enum EnumMenhirActivator implements IMetadata<EnumMenhirActivator>
{
	ANCIENT_AMBER("ancient_amber", "ancientAmber"),
	FOSSILIZED_EGG("fossilized_egg", "fossilizedEgg"),
	BROKEN_SPIRIT_MASK("broken_spirit_mask", "brokenSpiritMask"),
	RUSTED_OCTAEDRITE_FLAKE("rusted_octaedrite_flake", "rustedOctaedriteFlake"),
	SHIMMERING_TREE_STAR("shimmering_tree_star", "shimmeringTreeStar"),
	LEGENDARY_SPECTRUM_EYE("legendary_spectrum_eye", "legendarySpectrumEye"),
	PRIMITIVE_DUST("primitive_dust", "primitiveDust");
	
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