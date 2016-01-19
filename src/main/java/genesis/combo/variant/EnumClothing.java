package genesis.combo.variant;

import genesis.util.Constants;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public enum EnumClothing implements IMetadata<EnumClothing>
{
	CHITIN("chitin", 10, new int[]{2, 4, 3, 2}, 10),
	CAMOUFLAGE("camouflage", 5, new int[]{1, 2, 2, 1}, 5);
	
	final String name;
	final String unlocalizedName;
	final ArmorMaterial material;
	
	EnumClothing(String name, String unlocalizedName, int durability, int[] reductionAmounts, int enchantability)
	{
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.material = EnumHelper.addArmorMaterial(Constants.MOD_ID.toUpperCase() + "_" + name(), Constants.ASSETS_PREFIX + name,
				durability, reductionAmounts, enchantability);
	}
	
	EnumClothing(String name, int durability, int[] reductionAmounts, int enchantability)
	{
		this(name, name, durability, reductionAmounts, enchantability);
	}
	
	EnumClothing(String name, String unlocalizedName, int enchantability)
	{
		this(name, unlocalizedName, 0, new int[]{0, 0, 0, 0}, enchantability);
	}
	
	EnumClothing(String name, int enchantability)
	{
		this(name, name, enchantability);
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
	
	public ArmorMaterial getMaterial()
	{
		return material;
	}
}
