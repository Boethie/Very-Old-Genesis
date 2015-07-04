package genesis.metadata;

import net.minecraft.item.Item;

public enum EnumToolMaterial implements IMetadata
{
	OCTAEDRITE("octaedrite", 1, 145, 2.6F, 0.3F, 5),
	DOLERITE("dolerite", 1, 159, 3.8F, 0.3F, 5),
	RHYOLITE("rhyolite", 1, 173, 4.1F, 0.6F, 5),
	GRANITE("granite", 1, 180, 4.3F, 0.7F, 5),
	QUARTZITE("quartzite",  1, 187, 4.5F, 0.8F, 5),
	BROWN_FLINT("brown_flint", "brownFlint",  1, 194, 4.6F, 1.6F, 5);
	
	private final String name;
	private final String unlocalizedName;
	
	private final int harvestLevel;
	private final int uses;
	private final float efficiency;
	private final float entityDamage;
	private final int enchantability;

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
