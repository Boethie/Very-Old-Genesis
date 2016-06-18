package genesis.recipes;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.*;

import genesis.block.tileentity.crafting.CookingPotRecipeRegistry.*;
import genesis.combo.ItemsCeramicBowls;
import genesis.combo.variant.*;
import genesis.common.GenesisItems;
import genesis.util.*;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DyeCookingRecipe extends CookingPotRecipeBase
{
	public static final Map<Set<EnumDyeColor>, EnumDyeColor> CRAFTING_MAP;
	
	static
	{
		ImmutableMap.Builder<Set<EnumDyeColor>, EnumDyeColor> builder = ImmutableMap.builder();
		
		for (EnumDyeColor color : EnumDyeColor.values())
		{
			builder.put(Collections.singleton(color), color);
		}
		
		builder.put(MiscUtils.unmodSet(EnumDyeColor.WHITE, EnumDyeColor.RED), EnumDyeColor.PINK);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.PURPLE, EnumDyeColor.PINK), EnumDyeColor.MAGENTA);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.BLUE, EnumDyeColor.RED), EnumDyeColor.PURPLE);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.WHITE, EnumDyeColor.BLUE), EnumDyeColor.LIGHT_BLUE);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.GREEN, EnumDyeColor.BLUE), EnumDyeColor.CYAN);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.WHITE, EnumDyeColor.GREEN), EnumDyeColor.LIME);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.RED, EnumDyeColor.YELLOW), EnumDyeColor.ORANGE);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.BLUE, EnumDyeColor.ORANGE), EnumDyeColor.BROWN);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.GREEN, EnumDyeColor.RED), EnumDyeColor.BROWN);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.WHITE, EnumDyeColor.BLACK), EnumDyeColor.GRAY);
		builder.put(MiscUtils.unmodSet(EnumDyeColor.WHITE, EnumDyeColor.GRAY), EnumDyeColor.SILVER);
		
		CRAFTING_MAP = builder.build();
	}
	
	protected static EnumDyeColor getColor(ItemStack stack)
	{
		if (stack == null
				|| stack.stackSize <= 0
				|| GenesisItems.bowls.isStackOf(stack, ItemsCeramicBowls.DYE))
			return null;
		
		EnumPowder powder = GenesisItems.powders.getVariant(stack);
		
		if (powder != null && powder.getColor() != null)
		{
			return powder.getColor();
		}
		
		int[] ids = OreDictionary.getOreIDs(stack);
		
		for (int id : ids)
		{
			String name = OreDictionary.getOreName(id);
			
			for (EnumDyeColor color : EnumDyeColor.values())
			{
				if (name.equals(GenesisDye.getOreDictName(color)))
				{
					return color;
				}
			}
		}
		
		return null;
	}
	
	protected static EnumDyeColor getOutputColorFromColors(Set<EnumDyeColor> colors)
	{
		return CRAFTING_MAP.get(colors);
	}
	
	protected static EnumDyeColor getOutputColorFromSlots(Collection<? extends SlotModifier> slots)
	{
		return getOutputColorFromColors(
				slots.stream()
						.map((s) -> s.getStack())
						.filter((s) -> s != null)
						.map((s) -> getColor(s))
						.filter((c) -> c != null)
						.collect(Collectors.toCollection(() -> EnumSet.noneOf(EnumDyeColor.class))));
	}
	
	@Override
	public boolean isRecipeIngredient(ItemStack stack, InventoryCookingPot cookingPot)
	{
		return getColor(stack) != null;
	}
	
	@Override
	public boolean canCraft(InventoryCookingPot cookingPot)
	{
		return getOutputColorFromSlots(cookingPot.getIngredients()) != null && super.canCraft(cookingPot);
	}
	
	@Override
	public ItemStack getOutput(InventoryCookingPot cookingPot)
	{
		ItemStack stack = GenesisItems.bowls.getStack(getOutputColorFromSlots(cookingPot.getIngredients()), 0);
		
		for (SlotModifier slot : cookingPot.getIngredients())
		{
			ItemStack ingredient = slot.getStack();
			
			if (ingredient != null && ingredient.stackSize > 0)
			{
				stack.stackSize++;
			}
		}
		
		return stack;
	}
	
	@Override
	public void removeConsumed(InventoryCookingPot cookingPot, int countCrafted)
	{
		cookingPot.getInput().consume(countCrafted);
		
		for (SlotModifier slot : cookingPot.getIngredients())
		{
			slot.consume(1);
		}
	}
}
