package genesis.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeHelpers
{
	public static IRecipe tryGetMatchingRecipe(InventoryCrafting inventory, World world, Class<? extends IRecipe> recipeClass)
	{
		for (IRecipe recipe : CraftingManager.getInstance().getRecipeList())
		{
			if (recipeClass.isAssignableFrom(recipe.getClass()))
			{
				try
				{
					if (recipe.matches(inventory, world))
					{
						return recipe;
					}
				}
				catch (NullPointerException e) { }
			}
		}
		
		return null;
	}
	
	public static IRecipe tryGetMatchingRecipe(InventoryCrafting inventory, Class<? extends IRecipe> recipeClass)
	{
		return tryGetMatchingRecipe(inventory, null, recipeClass);
	}
}
