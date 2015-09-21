package genesis.metadata;

public enum EnumToolMaterial implements IMetadata
{
	OCTAEDRITE("octaedrite", 1, 145, 2.6F, 0.3F, 5),
	DOLERITE("dolerite", 1, 159, 3.8F, 0.3F, 5),
	RHYOLITE("rhyolite", 1, 173, 4.1F, 0.6F, 5),
	GRANITE("granite", 1, 180, 4.3F, 0.7F, 5),
	QUARTZ("quartz",  1, 187, 4.5F, 0.9F, 5),
	BROWN_FLINT("brown_flint", "brownFlint",  1, 194, 4.6F, 1.6F, 5),
	BLACK_FLINT("black_flint", "blackFlint",  1, 194, 4.6F, 1.6F, 5);
	
	final String name;
	final String unlocalizedName;
	
	final int harvestLevel;
	final int uses;
	final float efficiency;
	final float entityDamage;
	final int enchantability;

	EnumToolMaterial(String name, String unlocalizedName,
			int harvestLevel, int uses, float efficiency, float entityDamage, int enchantability)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		this.harvestLevel = harvestLevel;
		this.uses = uses;
		this.efficiency = efficiency;
		this.entityDamage = entityDamage;
		this.enchantability = enchantability;
	}

	EnumToolMaterial(String name,
			int harvestLevel, int uses, float efficiency, float entityDamage, int enchantability)
	{
		this(name, name, harvestLevel, uses, efficiency, entityDamage, enchantability);
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

	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
	
	public int getHarvestLevel()
	{
		return harvestLevel;
	}
	
	public int getUses()
	{
		return uses;
	}
	
	public float getEfficiency()
	{
		return efficiency;
	}
	
	public float getEntityDamage()
	{
		return entityDamage;
	}
	
	public int getEnchantability()
	{
		return enchantability;
	}
}
