package genesis.block.tileentity.crafting;

import genesis.common.*;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.util.*;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.item.*;

public class CookingPotRecipeRegistry
{
	public static interface CookingPotRecipe
	{
		public boolean isRecipeIngredient(ItemStack stack, InventoryCookingPot cookingPot);
		public boolean canCraft(InventoryCookingPot cookingPot);
		public void craft(InventoryCookingPot cookingPot);
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
			ItemStack invOutput = cookingPot.getOutput();
			ItemStack recipeOutput = getOutput(cookingPot);
			
			if (invOutput == null)
			{
				invOutput = recipeOutput.copy();
			}
			else
			{
				invOutput.stackSize += recipeOutput.stackSize;
			}
			
			cookingPot.setOutput(invOutput);
			
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
			Set<ItemStackKey> ingredientKeySet = Sets.newHashSet();
			
			for (SlotModifier ingSlot : cookingPot.getIngredients())
			{
				ItemStack ingStack = ingSlot.getStack();
				
				if (ingStack != null)
				{
					ingredientKeySet.add(new ItemStackKey(ingStack));
				}
			}
			
			if (ingredientKeySet.equals(ingredients.keySet()))
			{
				return output;
			}
			
			return null;
		}

		@Override
		public void removeConsumed(InventoryCookingPot cookingPot, int countCrafted)
		{
			ItemStack invInput = cookingPot.getInput();
			
			if (invInput.stackSize > 1)
			{
				invInput.stackSize--;
			}
			else
			{
				invInput = null;
			}
			
			cookingPot.setInput(invInput);
			
			//ItemStack[] invIngredients = cookingPot.getIngredients();
			
			// Get the number of stacks for each ingredient type.
			Map<ItemStackKey, Integer> countMap = new HashMap<ItemStackKey, Integer>(cookingPot.getIngredientSlotCount());
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
					
					ingSlot.modifySize(-size);
					
					sizeLeftMap.put(key, left - size);
					countMap.put(key, count - 1);
				}
			}
		}
	}
	
	public static interface InventoryCookingPot
	{
		public ItemStack getInput();
		public void setInput(ItemStack stack);
		
		public ItemStack getIngredient(int slot);
		public void setIngredient(int slot, ItemStack stack);
		public int getIngredientSlotCount();
		public List<? extends SlotModifier> getIngredients();
		
		public ItemStack getFuel();
		public void setFuel(ItemStack stack);
		
		public ItemStack getOutput();
		public boolean canOutputAccept(ItemStack stack);
		public void setOutput(ItemStack stack);
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
					ItemStack[] ingredients = new ItemStack[cookingPot.getIngredientSlotCount()];
					
					for (int i = 0; i < ingredients.length; i++)
					{
						ingredients[i] = cookingPot.getIngredient(i);
					}
					
					Genesis.logger.warn("CookingPotRecipeRegistry.getRecipe found multiple valid recipes for this cooking pot:");
					Genesis.logger.warn("Input stack: " + cookingPot.getInput());
					Genesis.logger.warn("Ingredient stacks: " + Stringify.stringifyArray(ingredients));
					Genesis.logger.warn("Output stack: " + cookingPot.getOutput());
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
