package genesis.item;

import genesis.block.BlockMoss;
import net.minecraft.item.ItemMultiTexture;

public class ItemMoss extends ItemMultiTexture
{
	public ItemMoss(BlockMoss moss)
	{
		super(moss, moss, (s) -> moss.getStateFromMeta(s.getMetadata()).getValue(BlockMoss.SOIL).getName());
	}
}
