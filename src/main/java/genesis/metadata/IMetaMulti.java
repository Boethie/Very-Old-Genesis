package genesis.metadata;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/** Multi-Item Metadata */
public interface IMetaMulti extends IMetadata
{
	public Block getBlock();

	public Item getItem();
}
