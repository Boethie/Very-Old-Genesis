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
			ItemStack output = getOutput(cookingPot);

			// Remove ingredients
			if (cookingPot.getOutput().put(output))
				removeConsumed(cookingPot, output.stackSize);
		}
	}

	public static class CookingPotRecipeShapeless extends CookingPotRecipeBase
	{
		protected final Map<ItemStackKey, ItemStack> ingredients;
		protected final boolean keepBowl;
		protected final ItemStack output;

		public CookingPotRecipeShapeless(ItemStack output, boolean keepBowl, ItemStack... ingredients)
		{
			Map<ItemStackKey, ItemStack> ingBuilder = new HashMap<>();

			if (ingredients.length < 1)
				throw new IllegalArgumentException("CookingPotRecipeShapeless was provided an empty array of ingredients.");

			for (ItemStack stack : ingredients)
			{
				ItemStack copy = stack.copy();
				ItemStack oldStack = ingBuilder.put(new ItemStackKey(copy), copy);

				if (oldStack != null)	// Combine stacks of the same item.
					copy.stackSize += oldStack.stackSize;
			}

			this.ingredients = ImmutableMap.copyOf(ingBuilder);
			this.keepBowl = keepBowl;
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
			if (ingredients.size() != cookingPot.getIngredients().stream().filter(s -> s.getStack() != null).count())
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
			ItemStack consumed = cookingPot.getInput().consume(1);

			if (keepBowl)
				cookingPot.getInputWaste().put(consumed.getItem().getContainerItem(consumed));

			// Get the number of stacks for each ingredient type.
			Map<ItemStackKey, Integer> countMap = new HashMap<>(cookingPot.getIngredients().size());
			Map<ItemStackKey, Integer> sizeLeftMap = new HashMap<>(ingredients.size());

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

					ingSlot.consume(size);

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
		SlotModifier getInputWaste();

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

	public static boolean isCookingPotItem(ItemStack stack) {
		return stack != null && new ItemStackKey(cookingPotItem).equals(new ItemStackKey(stack));
	}

	protected static List<CookingPotRecipe> recipes = new ArrayList<>();

	public static void registerRecipe(CookingPotRecipe recipe)
	{
		if (recipe == null)
		{
			throw new IllegalArgumentException("Cooking pot recipe passed for registering was null.");
		}

		recipes.add(recipe);
	}

	public static void registerShapeless(ItemStack output, boolean keepBowl, ItemStack... ingredients)
	{
		registerRecipe(new CookingPotRecipeShapeless(output, keepBowl, ingredients));
	}

	public static void registerShapeless(ItemStack output, ItemStack... ingredients)
	{
		registerShapeless(output, false, ingredients);
	}

	public static void registerShapeless(ItemStack output, boolean keepBowl, Collection<ItemStack> ingredients)
	{
		registerShapeless(output, keepBowl, ingredients.toArray(new ItemStack[ingredients.size()]));
	}

	public static void registerShapeless(ItemStack output, Collection<ItemStack> ingredients)
	{
		registerShapeless(output, false, ingredients);
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
