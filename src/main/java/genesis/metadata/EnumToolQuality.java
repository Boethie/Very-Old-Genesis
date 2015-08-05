package genesis.metadata;

import genesis.util.Constants.Unlocalized;

public enum EnumToolQuality implements IMetadata
{
	NONE("", 1, 1, 1, 1),
	WEAK("", 0.065F, 0.25F, 0.25F, 0.1F),
	CHIPPED("chipped", 1, 1, 1, 1),
	POLISHED("polished", 1.5F, 1.5F, 1.5F, 1.5F),
	SHARPENED("sharpened", 1.5F, 1.5F, 1.5F, 1.5F);

	public static final String UNLOC_PREFIX = "item." + Unlocalized.PREFIX + Unlocalized.Section.TOOL_QUALITY;
	
	private final String name;
	private final String unlocalizedName;
	
	private final float usesMult;
	private final float efficiencyMult;
	private final float entityDamageMult;
	private final float enchantabilityMult;

	EnumToolQuality(String name, String unlocalizedName,
			float usesMult, float efficiencyMult, float entityDamageMult, float enchantabilityMult)
	{
		this.name = name != null ? name : "";
		this.unlocalizedName = unlocalizedName != null ? unlocalizedName : "";
		
		this.usesMult = usesMult;
		this.efficiencyMult = efficiencyMult;
		this.entityDamageMult = entityDamageMult;
		this.enchantabilityMult = enchantabilityMult;
	}

	EnumToolQuality(String name,
			float usesMult, float efficiencyMult, float entityDamageMult, float enchantabilityMult)
	{
		this(name, name, usesMult, efficiencyMult, entityDamageMult, enchantabilityMult);
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
	public boolean hasUnlocalizedName()
	{
		return !"".equals(unlocalizedName);
	}

	@Override
	public String getUnlocalizedName()
	{
		return UNLOC_PREFIX + unlocalizedName;
	}
	
	public float getUsesMult()
	{
		return usesMult;
	}
	
	public float getEfficiencyMult()
	{
		return efficiencyMult;
	}
	
	public float getEntityDamageMult()
	{
		return entityDamageMult;
	}
	
	public float getEnchantabilityMult()
	{
		return enchantabilityMult;
	}
}
