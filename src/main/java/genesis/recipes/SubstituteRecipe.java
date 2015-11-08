package genesis.recipes;

import com.google.common.base.Function;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class SubstituteRecipe implements IRecipe
{
	protected final Class<? extends IRecipe> recipeClass;
	protected final Function<ItemStack, ItemStack> substitution;
	
	public SubstituteRecipe(Class<? extends IRecipe> recipeClass, Function<ItemStack, ItemStack> substitution)
	{
		this.recipeClass = recipeClass;
		this.substitution = substitution;
	}
	
	protected InventoryCrafting getSubstituteInventory(InventoryCrafting inventory)
	{
		return new SubstituteCraftingGrid(inventory, substitution);
	}
	
	@Override
	public boolean matches(InventoryCrafting inventory, World world)
	{
		return RecipeHelpers.tryGetMatchingRecipe(getSubstituteInventory(inventory), world, recipeClass) != null;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory)
	{
		inventory = getSubstituteInventory(inventory);
		return RecipeHelpers.tryGetMatchingRecipe(inventory, recipeClass).getCraftingResult(inventory);
	}
	
	@Override
	public int getRecipeSize()
	{
		return 10;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
	
	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inventory)
	{
		return RecipeHelpers.tryGetMatchingRecipe(getSubstituteInventory(inventory), recipeClass).getRemainingItems(inventory);
	}
}
