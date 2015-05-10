package genesis.block.tileentity.crafting;

import genesis.common.Genesis;
import genesis.common.GenesisItems;
import genesis.metadata.ItemsCeramicBowls.EnumCeramicBowls;
import genesis.util.Stringify;

import java.util.*;

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
		
		public abstract ItemStack removeIngredient(ItemStack invIngredient, IInventoryCookingPot cookingPot);
		
		public void removeIngredients(IInventoryCookingPot cookingPot)
		{
			ItemStack[] invIngredients = cookingPot.getIngredients();

			for (int i = 0; i < invIngredients.length; i++)
			{
				ItemStack newStack = removeIngredient(invIngredients[i], cookingPot);
				
				if (newStack.stackSize <= 0)
				{
					newStack = null;
				}
				
				invIngredients[i] = newStack;
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
				this.ingredients.put(new RecipeKey(stack), stack);
			}
			
			this.output = output;
		}
		
		@Override
		public ItemStack getOutput(IInventoryCookingPot cookingPot)
		{
			Set<RecipeKey> ingredientKeySet = new HashSet();
			
			for (ItemStack ingredient : cookingPot.getIngredients())
			{
				if (ingredient != null)
				{
					ingredientKeySet.add(new RecipeKey(ingredient));
				}
			}
			
			if (ingredientKeySet.equals(ingredients.keySet()))
			{
				return output;
			}
			
			return null;
		}
		
		@Override
		public ItemStack removeIngredient(ItemStack invIngredient, IInventoryCookingPot cookingPot)
		{
			invIngredient.stackSize -= ingredients.get(new RecipeKey(invIngredient)).stackSize;
			return invIngredient;
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
	
	protected static ItemStack cookingPotItem = GenesisItems.ceramic_bowls.getStack(EnumCeramicBowls.WATER_BOWL);
	
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
