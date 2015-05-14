package genesis.block.tileentity.crafting;

import genesis.common.Genesis;
import genesis.common.GenesisItems;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.util.Stringify;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;

public class CookingPotRecipeRegistry
{
	protected static boolean stackContainsOther(ItemStack stack, ItemStack other)
	{
		if (stack.isItemEqual(other) && ItemStack.areItemStackTagsEqual(stack, other))
		{
			if (stack.stackSize <= other.stackSize)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static class RecipeKey
	{
		protected final Item item;
		protected final int metadata;
		protected final NBTTagCompound compound;
		
		public RecipeKey(Item item, int metadata, NBTTagCompound compound)
		{
			if (item == null)
			{
				throw new IllegalArgumentException("Item passed to constructor was null.");
			}
			
			this.item = item;
			this.metadata = metadata;
			this.compound = compound;
		}
		
		public RecipeKey(ItemStack stack)
		{
			this(stack.getItem(), stack.getItemDamage(), stack.getTagCompound());
		}
		
		@Override
		public int hashCode()
		{
			return item.hashCode() + (metadata << 16) + (compound != null ? compound.hashCode() : 0);
		}
		
		@Override
		public boolean equals(Object other)
		{
			if (this == other)
			{
				return true;
			}
			
			if (other instanceof RecipeKey)
			{
				RecipeKey otherKey = (RecipeKey) other;
				
				if (item.equals(otherKey.item) && metadata == otherKey.metadata)
				{
					if (compound == otherKey.compound || (compound != null && compound.equals(otherKey.compound)))
					{
						return true;
					}
				}
			}
			
			return false;
		}
	}
	
	public static interface ICookingPotRecipe
	{
		public boolean isRecipeIngredient(ItemStack stack, IInventoryCookingPot cookingPot);
		public boolean canCraft(IInventoryCookingPot cookingPot);
		public ItemStack getOutput(IInventoryCookingPot cookingPot);
		public void craft(IInventoryCookingPot cookingPot);
	}
	
	public static abstract class CookingPotRecipeBase implements ICookingPotRecipe
	{
		public boolean canCraft(IInventoryCookingPot cookingPot)
		{
			return getOutput(cookingPot) != null;
		}
		
		public abstract ItemStack getOutput(IInventoryCookingPot cookingPot);
		
		public abstract ItemStack[] doRemoveIngredients(IInventoryCookingPot cookingPot);
		
		public void removeIngredients(IInventoryCookingPot cookingPot)
		{
			ItemStack[] invIngredients = doRemoveIngredients(cookingPot);
			
			for (int i = 0; i < invIngredients.length; i++)
			{
				ItemStack newStack = invIngredients[i];	// Replace stacks with size less than 1 with null.
				invIngredients[i] = (newStack != null && newStack.stackSize <= 0) ? null : newStack;
			}
			
			cookingPot.setIngredients(invIngredients);
		}
		
		public void craft(IInventoryCookingPot cookingPot)
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
			
			removeIngredients(cookingPot);
		}
	}
	
	public static class CookingPotRecipeShapeless extends CookingPotRecipeBase
	{
		protected Map<RecipeKey, ItemStack> ingredients;
		protected ItemStack output;
		
		public CookingPotRecipeShapeless(ItemStack output, ItemStack... ingredients)
		{
			this.ingredients = new HashMap();
			
			for (ItemStack stack : ingredients)
			{
				ItemStack copy = stack.copy();
				ItemStack oldStack = this.ingredients.put(new RecipeKey(copy), copy);
				
				if (oldStack != null)	// Combine stacks of the same item.
				{
					copy.stackSize += oldStack.stackSize;
				}
			}
			
			this.output = output;
		}
		
		@Override
		public boolean isRecipeIngredient(ItemStack stack, IInventoryCookingPot cookingPot)
		{
			return ingredients.containsKey(new RecipeKey(stack));
		}
		
		@Override
		public ItemStack getOutput(IInventoryCookingPot cookingPot)
		{
			ItemStack[] invIngredients = cookingPot.getIngredients();
			Set<RecipeKey> ingredientKeySet = new HashSet();
			
			for (ItemStack invIngredient : invIngredients)
			{
				if (invIngredient != null)
				{
					ingredientKeySet.add(new RecipeKey(invIngredient));
				}
			}
			
			if (ingredientKeySet.equals(ingredients.keySet()))
			{
				return output;
			}
			
			return null;
		}

		@Override
		public ItemStack[] doRemoveIngredients(IInventoryCookingPot cookingPot)
		{
			ItemStack[] invIngredients = cookingPot.getIngredients();
			
			// Get the number of stacks for each ingredient type.
			Map<RecipeKey, Integer> countMap = new HashMap(invIngredients.length);
			Map<RecipeKey, Integer> sizeLeftMap = new HashMap(ingredients.size());
			
			for (Map.Entry<RecipeKey, ItemStack> entry : ingredients.entrySet())
			{
				sizeLeftMap.put(entry.getKey(), entry.getValue().stackSize);
			}
			
			for (int i = 0; i < invIngredients.length; i++)
			{
				ItemStack ingStack = invIngredients[i];
				
				if (ingStack != null)
				{
					RecipeKey key = new RecipeKey(ingStack);
					Integer count = countMap.get(key);
					countMap.put(key, count == null ? 1 : count + 1);
				}
			}
			
			for (int i = 0; i < invIngredients.length; i++)
			{
				ItemStack newStack = invIngredients[i];
				
				if (newStack != null)
				{
					RecipeKey key = new RecipeKey(newStack);
					
					int left = sizeLeftMap.get(key);
					int count = countMap.get(key);
					int size = left / count;
					
					newStack.stackSize -= size;
					
					sizeLeftMap.put(key, left - size);
					countMap.put(key, count - 1);
					
					invIngredients[i] = newStack;
				}
			}
			
			return invIngredients;
		}
	}
	
	public static interface IInventoryCookingPot
	{
		public ItemStack getInput();
		public void setInput(ItemStack stack);
		
		public ItemStack[] getIngredients();
		public void setIngredients(ItemStack[] stacks);
		
		public ItemStack getFuel();
		public void setFuel(ItemStack stack);
		
		public ItemStack getOutput();
		public void setOutput(ItemStack stack);
	}
	
	protected static ItemStack cookingPotItem = GenesisItems.bowls.getStack(EnumCeramicBowls.WATER_BOWL);
	
	public static boolean isCookingPotItem(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}
		
		return new RecipeKey(cookingPotItem).equals(new RecipeKey(stack));
	}
	
	protected static List<ICookingPotRecipe> recipes = new ArrayList();
	
	public static void registerRecipe(ICookingPotRecipe recipe)
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
	
	public static boolean isRecipeIngredient(ItemStack stack, IInventoryCookingPot cookingPot)
	{
		for (ICookingPotRecipe recipe: recipes)
		{
			if (recipe.isRecipeIngredient(stack, cookingPot))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static ICookingPotRecipe getRecipe(IInventoryCookingPot cookingPot)
	{
		ICookingPotRecipe output = null;
		
		for (ICookingPotRecipe recipe : recipes)
		{
			if (recipe.canCraft(cookingPot))
			{
				if (output != null)
				{
					Genesis.logger.warn("CookingPotRecipeRegistry.getRecipe found multiple valid recipes for this cooking pot:");
					Genesis.logger.warn("Input stack: " + cookingPot.getInput());
					Genesis.logger.warn("Ingredient stacks: " + Stringify.stringify(cookingPot.getIngredients()));
					Genesis.logger.warn("Output stack: " + cookingPot.getOutput());
				}
				
				output = recipe;
			}
		}
		
		return output;
	}
	
	public static boolean hasRecipe(IInventoryCookingPot cookingPot)
	{
		return getRecipe(cookingPot) != null;
	}
	
	public static ItemStack getResult(IInventoryCookingPot cookingPot)
	{
		ICookingPotRecipe recipe = getRecipe(cookingPot);
		
		if (recipe != null && recipe.canCraft(cookingPot))
		{
			return recipe.getOutput(cookingPot);
		}
		
		return null;
	}
}
