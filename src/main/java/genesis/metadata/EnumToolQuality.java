package genesis.metadata;

import genesis.util.Constants.Unlocalized;

public enum EnumToolQuality implements IMetadata<EnumToolQuality>
{
	NONE("", 1, 1, 1, 1),
	WEAK("", 0.65F, 0.25F, 0.25F, 0.1F),
	CHIPPED("chipped", 1, 1, 1, 1),
	POLISHED("polished", 1.5F, 1.5F, 1.5F, 1.5F),
	SHARPENED("sharpened", 1.5F, 1.5F, 1.5F, 1.5F);

	public static final String UNLOC_PREFIX = Unlocalized.ITEM_PREFIX + Unlocalized.Section.TOOL_QUALITY;
	
	final String name;
	final String unlocalizedName;
	
	final float usesMult;
	final float efficiencyMult;
	final float entityDamageMult;
	final float enchantabilityMult;

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
