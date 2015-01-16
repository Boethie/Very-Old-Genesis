package genesis.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemGenesisMetadata extends ItemGenesis {
    private final String[] names;
    private final String[] unlocalizedNames;

    public ItemGenesisMetadata(IMetadata[] metadataItems) {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);

        names = new String[metadataItems.length];
        unlocalizedNames = new String[metadataItems.length];

        for (int metadata = 0; metadata < metadataItems.length; metadata++) {
            names[metadata] = metadataItems[metadata].getName();
            unlocalizedNames[metadata] = metadataItems[metadata].getUnlocalizedName();
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
