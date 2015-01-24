package genesis.item;

import genesis.util.Metadata;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemStack;

public class ItemBlockColored extends ItemColored
{
	public ItemBlockColored(Block block)
	{
		super(block, false);
	}

	public ItemBlockColored(Block block, Class<? extends IMetadata> clazz)
	{
		super(block, true);
		ArrayList<String> subtypeNames = new ArrayList<String>();
		for (IMetadata meta : Metadata.getLookup(clazz))
		{
			subtypeNames.add(meta.getUnlocalizedName());
		}
		setSubtypeNames(subtypeNames.toArray(new String[subtypeNames.size()]));
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        return getBlock().getBlockColor();
    }
}
