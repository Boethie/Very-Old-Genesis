package genesis.recipes;

import java.util.*;

import com.google.common.collect.*;

import genesis.block.tileentity.crafting.CookingPotRecipeRegistry.*;
import genesis.combo.ItemsCeramicBowls;
import genesis.combo.variant.EnumPowder;
import genesis.combo.variant.GenesisDye;
import genesis.common.GenesisItems;
import genesis.util.SlotModifier;
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
		
		builder.put(ImmutableSet.of(EnumDyeColor.WHITE, EnumDyeColor.RED), EnumDyeColor.PINK);
		builder.put(ImmutableSet.of(EnumDyeColor.PURPLE, EnumDyeColor.PINK), EnumDyeColor.MAGENTA);
		builder.put(ImmutableSet.of(EnumDyeColor.BLUE, EnumDyeColor.RED), EnumDyeColor.PURPLE);
		builder.put(ImmutableSet.of(EnumDyeColor.WHITE, EnumDyeColor.BLUE), EnumDyeColor.LIGHT_BLUE);
		builder.put(ImmutableSet.of(EnumDyeColor.GREEN, EnumDyeColor.BLUE), EnumDyeColor.CYAN);
		builder.put(ImmutableSet.of(EnumDyeColor.WHITE, EnumDyeColor.GREEN), EnumDyeColor.LIME);
		builder.put(ImmutableSet.of(EnumDyeColor.RED, EnumDyeColor.YELLOW), EnumDyeColor.ORANGE);
		builder.put(ImmutableSet.of(EnumDyeColor.BLUE, EnumDyeColor.ORANGE), EnumDyeColor.BROWN);
		builder.put(ImmutableSet.of(EnumDyeColor.GREEN, EnumDyeColor.RED), EnumDyeColor.BROWN);
		builder.put(ImmutableSet.of(EnumDyeColor.WHITE, EnumDyeColor.BLACK), EnumDyeColor.GRAY);
		builder.put(ImmutableSet.of(EnumDyeColor.WHITE, EnumDyeColor.GRAY), EnumDyeColor.SILVER);
		
		CRAFTING_MAP = builder.build();
	}
	
	protected static EnumDyeColor getColor(ItemStack stack)
	{
		if (stack == null || stack.stackSize <= 0 ||
			GenesisItems.bowls.isStackOf(stack, ItemsCeramicBowls.DYE))
		{
			return null;
		}
		
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
	
	protected static EnumDyeColor getOutputColorFromColors(EnumDyeColor... colors)
	{
		Set<EnumDyeColor> colorSet = Sets.newHashSet();
		
		for (EnumDyeColor color : colors)
		{
			if (color != null)
			{
				colorSet.add(color);
			}
		}
		
		return CRAFTING_MAP.get(colorSet);
	}
	
	protected static EnumDyeColor getOutputColorFromStacks(Iterable<ItemStack> stacks)
	{
		Set<EnumDyeColor> colorSet = Sets.newHashSet();
		
		for (ItemStack stack : stacks)
		{
			EnumDyeColor color = getColor(stack);
			
			if (color != null)
			{
				colorSet.add(color);
			}
		}
		
		return getOutputColorFromColors(colorSet);
	}
	
	protected static EnumDyeColor getOutputColorFromSlots(Iterable<? extends SlotModifier> slots)
	{
		Set<EnumDyeColor> colorSet = Sets.newHashSet();
		
		for (SlotModifier slot : slots)
		{
			EnumDyeColor color = getColor(slot.getStack());
			
			if (color != null)
			{
				colorSet.add(color);
			}
		}
		
		return getOutputColorFromColors(colorSet);
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
		cookingPot.getInput().incrementSize(-countCrafted);
		
		for (SlotModifier slot : cookingPot.getIngredients())
		{
			slot.incrementSize(-1);
		}
	}
}
