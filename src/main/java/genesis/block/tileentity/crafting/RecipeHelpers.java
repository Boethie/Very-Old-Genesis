package genesis.block.tileentity.crafting;

import genesis.util.ItemStackKey;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHelpers
{
	public boolean areRecipeStacksEqual(ItemStack stack1, ItemStack stack2)
	{
		return new ItemStackKey(stack1).equals(new ItemStackKey(stack2));
	}
}
