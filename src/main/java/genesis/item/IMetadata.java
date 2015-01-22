package genesis.item;

import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

public interface IMetadata extends IStringSerializable
{
	public String getUnlocalizedName();

	public Item getItem();
}
