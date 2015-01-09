package genesis.item;

import genesis.common.IMetadata;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemGenesisMetadata extends ItemGenesis {
    private final String[] names;
    private final String[] unlocalizedNames;

    public ItemGenesisMetadata(IMetadata[] metadatas) {
        setHasSubtypes(true);
        setMaxDamage(0);

        names = new String[metadatas.length];
        unlocalizedNames = new String[metadatas.length];

        for (int metadata = 0; metadata < metadatas.length; metadata++) {
            names[metadata] = metadatas[metadata].getName();
            unlocalizedNames[metadata] = metadatas[metadata].getUnlocalizedName();
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + getUnlocalizedNames()[MathHelper.clamp_int(stack.getMetadata(), 0, getUnlocalizedNames().length)];
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (int i = 0; i < getUnlocalizedNames().length; i++) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }

    public String[] getNames() {
        return names;
    }

    public String[] getUnlocalizedNames() {
        return unlocalizedNames;
    }
}
