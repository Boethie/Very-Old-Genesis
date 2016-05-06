package genesis.combo;

import genesis.combo.variant.*;
import genesis.combo.variant.BowVariants.*;
import genesis.item.ItemBowMulti;
import genesis.util.Constants;
import genesis.util.ReflectionUtils;
import genesis.util.Constants.Unlocalized;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BowItems extends VariantsCombo<BowVariant, Block, ItemBowMulti<BowVariant>>
{
	public BowItems()
	{
		super(ObjectType.createItem(BowVariant.class, "bow", ReflectionUtils.convertClass(ItemBowMulti.class)),
				BowVariant.class, BowVariants.getAll());
		
		setNames(Constants.MOD_ID, Unlocalized.PREFIX);
		
		getObjectType().setShouldRegisterVariantModels(false);
	}
	
	/**
	 * Get an item stack containing the tool item of the specified {@link ToolObjectType}, material and quality.
	 */
	public ItemStack getStack(EnumBowType bowType, EnumTree variant, int stackSize)
	{
		return getStack(BowVariants.get(bowType, variant), stackSize);
	}
	
	/**
	 * Get an item stack containing the tool item of the specified {@link ToolObjectType}, material and quality with a stack size of 1.
	 */
	public ItemStack getStack(EnumBowType bowType, EnumTree variant)
	{
		return getStack(bowType, variant, 1);
	}
	
	/**
	 * @return Whether the stack is of the specified {@link EnumBowType} and variant.
	 */
	public boolean isStackOf(ItemStack stack, EnumBowType bowType, EnumTree variant)
	{
		if (!isStackOf(stack))
			return false;
		
		BowVariant bow = getVariant(stack);
		
		return bow.getBowType() == bowType && bow.getVariant() == variant;
	}
	
	/**
	 * @return Whether the stack is of the specified {@link EnumBowType}.
	 */
	public boolean isStackOf(ItemStack stack, EnumBowType bowType)
	{
		if (!isStackOf(stack))
			return false;
		
		BowVariant bow = getVariant(stack);
		
		return bow.getBowType() == bowType;
	}
}
