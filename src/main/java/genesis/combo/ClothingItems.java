package genesis.combo;

import com.google.common.collect.ImmutableList;

import genesis.combo.variant.EnumClothing;
import genesis.item.ItemGenesisArmor;
import genesis.util.Constants;
import genesis.util.Constants.Unlocalized;
import net.minecraft.block.Block;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ClothingItems extends VariantsOfTypesCombo<EnumClothing>
{
	public static final ObjectType<EnumClothing, Block, ItemGenesisArmor> HELMET =
			ObjectType.createItem(EnumClothing.class, "helmet", ItemGenesisArmor.class)
			.setItemArguments(EntityEquipmentSlot.HEAD)
			.setTypeNamePosition(TypeNamePosition.POSTFIX);
	public static final ObjectType<EnumClothing, Block, ItemGenesisArmor> CHESTPLATE =
			ObjectType.createItem(EnumClothing.class, "chestplate", ItemGenesisArmor.class)
					.setItemArguments(EntityEquipmentSlot.CHEST)
					.setTypeNamePosition(TypeNamePosition.POSTFIX);
	public static final ObjectType<EnumClothing, Block, ItemGenesisArmor> LEGGINGS =
			ObjectType.createItem(EnumClothing.class, "leggings", ItemGenesisArmor.class)
					.setItemArguments(EntityEquipmentSlot.LEGS)
					.setTypeNamePosition(TypeNamePosition.POSTFIX);
	public static final ObjectType<EnumClothing, Block, ItemGenesisArmor> BOOTS =
			ObjectType.createItem(EnumClothing.class, "boots", ItemGenesisArmor.class)
					.setItemArguments(EntityEquipmentSlot.FEET)
					.setTypeNamePosition(TypeNamePosition.POSTFIX);
	
	public ClothingItems()
	{
		super(ImmutableList.of(HELMET, CHESTPLATE, LEGGINGS, BOOTS),
				EnumClothing.class, ImmutableList.copyOf(EnumClothing.values()));
		
		setNames(Constants.MOD_ID, Unlocalized.CLOTHING);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack, String base)
	{
		return super.getUnlocalizedName(stack, "item." + getUnlocalizedPrefix()) + "." + base;
	}
	
	public ItemStack getHelmet(EnumClothing variant)
	{
		return getStack(HELMET, variant);
	}
	
	public ItemStack getChestplate(EnumClothing variant)
	{
		return getStack(CHESTPLATE, variant);
	}
	
	public ItemStack getLeggings(EnumClothing variant)
	{
		return getStack(LEGGINGS, variant);
	}
	
	public ItemStack getBoots(EnumClothing variant)
	{
		return getStack(BOOTS, variant);
	}
}
