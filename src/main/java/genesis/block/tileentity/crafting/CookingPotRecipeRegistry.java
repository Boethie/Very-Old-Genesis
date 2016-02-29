package genesis.block.tileentity.crafting;

import genesis.combo.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.common.*;
import genesis.util.*;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.item.*;

public class CookingPotRecipeRegistry
{
	public interface CookingPotRecipe
	{
		boolean isRecipeIngredient(ItemStack stack, InventoryCookingPot cookingPot);
		boolean canCraft(InventoryCookingPot cookingPot);
		void craft(InventoryCookingPot cookingPot);
	}
	
	public static abstract class CookingPotRecipeBase implements CookingPotRecipe
	{
		protected abstract ItemStack getOutput(InventoryCookingPot cookingPot);
		
		@Override
		public boolean canCraft(InventoryCookingPot cookingPot)
		{
			ItemStack output = getOutput(cookingPot);
			return output != null && cookingPot.canOutputAccept(output);
		}
		
		protected abstract void removeConsumed(InventoryCookingPot cookingPot, int countCrafted);
		
		@Override
		public void craft(InventoryCookingPot cookingPot)
		{
			SlotModifier invOutput = cookingPot.getOutput();
			ItemStack recipeOutput = getOutput(cookingPot);
			
			if (invOutput.getStack() == null)
				invOutput.set(recipeOutput.copy());
			else
				invOutput.incrementSize(recipeOutput.stackSize);
			
			// Remove ingredients
			removeConsumed(cookingPot, recipeOutput.stackSize);
		}
	}
	
	public static class CookingPotRecipeShapeless extends CookingPotRecipeBase
	{
		protected Map<ItemStackKey, ItemStack> ingredients;
		protected ItemStack output;
		
		public CookingPotRecipeShapeless(ItemStack output, ItemStack... ingredients)
		{
			this.ingredients = Maps.newHashMap();
			
			if (ingredients.length < 1)
				throw new IllegalArgumentException("CookingPotRecipeShapeless was provided an empty array of ingredients.");
			
			for (ItemStack stack : ingredients)
			{
				ItemStack copy = stack.copy();
				ItemStack oldStack = this.ingredients.put(new ItemStackKey(copy), copy);
				
				if (oldStack != null)	// Combine stacks of the same item.
				{
					copy.stackSize += oldStack.stackSize;
				}
			}
			
			this.output = output;
		}
		
		@Override
		public boolean isRecipeIngredient(ItemStack stack, InventoryCookingPot cookingPot)
		{
			return ingredients.containsKey(new ItemStackKey(stack));
		}
		
		@Override
		public ItemStack getOutput(InventoryCookingPot cookingPot)
		{
			if (ingredients.size() != FluentIterable.from(cookingPot.getIngredients()).filter(s -> s.getStack() != null).size())
				return null;
			
			for (SlotModifier ingSlot : cookingPot.getIngredients())
			{
				ItemStack stack = ingSlot.getStack();
				if (stack != null && !ingredients.containsKey(new ItemStackKey(stack)))
					return null;
			}
			
			return output;
		}
		
		@Override
		public void removeConsumed(InventoryCookingPot cookingPot, int countCrafted)
		{
			cookingPot.getInput().incrementSize(-1);
			
			// Get the number of stacks for each ingredient type.
			Map<ItemStackKey, Integer> countMap = new HashMap<ItemStackKey, Integer>(cookingPot.getIngredients().size());
			Map<ItemStackKey, Integer> sizeLeftMap = new HashMap<ItemStackKey, Integer>(ingredients.size());
			
			for (Map.Entry<ItemStackKey, ItemStack> entry : ingredients.entrySet())
			{
				sizeLeftMap.put(entry.getKey(), entry.getValue().stackSize);
			}
			
			for (SlotModifier ingSlot : cookingPot.getIngredients())
			{
				ItemStack ingStack = ingSlot.getStack();
				
				if (ingStack != null)
				{
					ItemStackKey key = new ItemStackKey(ingStack);
					Integer count = countMap.get(key);
					countMap.put(key, count == null ? 1 : count + 1);
				}
			}
			
			for (SlotModifier ingSlot : cookingPot.getIngredients())
			{
				ItemStack ingStack = ingSlot.getStack();
				
				if (ingStack != null)
				{
					ItemStackKey key = new ItemStackKey(ingStack);
					
					int left = sizeLeftMap.get(key);
					int count = countMap.get(key);
					int size = left / count;
					
					ingSlot.incrementSize(-size);
					
					sizeLeftMap.put(key, left - size);
					countMap.put(key, count - 1);
				}
			}
		}
	}
	
	public interface InventoryCookingPot
	{
		//ItemStack getInput();
		//void setInput(ItemStack stack);
		SlotModifier getInput();
		
		
		List<? extends SlotModifier> getIngredients();
		
		//ItemStack getFuel();
		//void setFuel(ItemStack stack);
		SlotModifier getFuel();
		
		//ItemStack getOutput();
		//boolean canOutputAccept(ItemStack stack);
		//void setOutput(ItemStack stack);
		SlotModifier getOutput();
		
		boolean canOutputAccept(ItemStack stack);
	}
	
	protected static ItemStack cookingPotItem = GenesisItems.bowls.getStack(EnumCeramicBowls.WATER_BOWL);
	
	public static boolean isCookingPotItem(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}
		
		return new ItemStackKey(cookingPotItem).equals(new ItemStackKey(stack));
	}
	
	protected static List<CookingPotRecipe> recipes = Lists.newArrayList();
	
	public static void registerRecipe(CookingPotRecipe recipe)
	{
		if (recipe == null)
		{
			throw new IllegalArgumentException("Cooking pot recipe passed for registering was null.");
		}
		
		recipes.add(recipe);
	}
	
	public static void registerShapeless(ItemStack output, ItemStack... ingredients)
	{
		registerRecipe(new CookingPotRecipeShapeless(output, ingredients));
	}
	
	public static void registerShapeless(ItemStack output, Collection<ItemStack> ingredients)
	{
		registerShapeless(output, ingredients.toArray(new ItemStack[ingredients.size()]));
	}
	
	public static boolean isRecipeIngredient(ItemStack stack, InventoryCookingPot cookingPot)
	{
		for (CookingPotRecipe recipe: recipes)
		{
			if (recipe.isRecipeIngredient(stack, cookingPot))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static CookingPotRecipe getRecipe(InventoryCookingPot cookingPot)
	{
		CookingPotRecipe output = null;
		
		for (CookingPotRecipe recipe : recipes)
		{
			if (recipe.canCraft(cookingPot))
			{
				if (output != null)
				{
					Genesis.logger.warn("CookingPotRecipeRegistry.getRecipe found multiple valid recipes for this cooking pot:");
					Genesis.logger.warn("Input: " + cookingPot.getInput());
					Genesis.logger.warn("Ingredients: " + cookingPot.getIngredients());
					Genesis.logger.warn("Output: " + cookingPot.getOutput());
				}
				else
				{
					output = recipe;
				}
			}
		}
		
		return output;
	}
	
	public static boolean hasRecipe(InventoryCookingPot cookingPot)
	{
		return getRecipe(cookingPot) != null;
	}
}
