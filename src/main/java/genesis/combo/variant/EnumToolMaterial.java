package genesis.combo.variant;

public enum EnumToolMaterial implements IArrowMetadata<EnumToolMaterial>
{
	OCTAEDRITE("octaedrite", 1, 145, 2.6F, 0.3F, 5, 0.1F),
	DOLERITE("dolerite", 1, 159, 3.8F, 0.3F, 5, 0.1F),
	RHYOLITE("rhyolite", 1, 173, 4.1F, 0.6F, 5, 0.15F),
	GRANITE("granite", 1, 180, 4.3F, 0.7F, 5, 0.175F),
	QUARTZ("quartz", 1, 187, 4.5F, 0.9F, 5, 0.2F),
	BROWN_FLINT("brown_flint", "brownFlint", 1, 194, 4.6F, 1.6F, 5, 0.3F),
	BLACK_FLINT("black_flint", "blackFlint", 1, 194, 4.6F, 1.6F, 5, 0.3F);
	
	final String name;
	final String unlocalizedName;
	
	final int harvestLevel;
	final int uses;
	final float efficiency;
	final float entityDamage;
	final int enchantability;
	
	final float mass;
	
	EnumToolMaterial(String name, String unlocalizedName,
			int harvestLevel, int uses, float efficiency,
			float entityDamage, 
			int enchantability,
			float mass)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		
		this.harvestLevel = harvestLevel;
		this.uses = uses;
		this.efficiency = efficiency;
		this.entityDamage = entityDamage;
		this.enchantability = enchantability;
		
		this.mass = mass;
	}

	EnumToolMaterial(String name,
			int harvestLevel, int uses, float efficiency, float entityDamage, int enchantability, float mass)
	{
		this(name, name, harvestLevel, uses, efficiency, entityDamage, enchantability, mass);
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
	
	@Override
	public float getMass()
	{
		return mass;
	}
}
