package genesis.metadata;

import net.minecraft.item.Item;

/** Single-Item Metadata */
public interface IMetaSingle extends IMetadata
{
	public Item getItem();
}
