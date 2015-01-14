package genesis.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public interface IMetadataItem extends IStringSerializable {
    public String getUnlocalizedName();

    public int getMetadata();

    public ItemStack createStack(int amount);
}
