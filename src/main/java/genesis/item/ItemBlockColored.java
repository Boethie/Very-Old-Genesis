package genesis.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;

public class ItemBlockColored extends ItemColored
{
	public ItemBlockColored(Block block)
	{
		super(block, false);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return getBlock().getBlockColor();
	}
}
