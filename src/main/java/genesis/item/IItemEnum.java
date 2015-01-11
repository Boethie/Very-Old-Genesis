package genesis.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public interface IItemEnum extends IStringSerializable {
    public String getUnlocalizedName();

    public ItemStack createStack(int amount);
}
