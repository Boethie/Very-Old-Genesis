package genesis.block.tileentity.crafting;

import genesis.util.ItemStackKey;
import net.minecraft.item.ItemStack;

public class RecipeHelpers
{
	public boolean areRecipeStacksEqual(ItemStack stack1, ItemStack stack2)
	{
		return new ItemStackKey(stack1).equals(new ItemStackKey(stack2));
	}
}
