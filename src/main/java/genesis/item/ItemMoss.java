package genesis.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;

public class ItemMoss extends ItemColored
{
	public ItemMoss(Block block)
	{
		super(block, true);
	}
	
	/*@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return block.getBlockColor();
	}*/
}
