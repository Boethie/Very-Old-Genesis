package genesis.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemGenesisMetadata extends ItemGenesis {
    private final String[] names;

    public ItemGenesisMetadata(String... names) {
        setHasSubtypes(true);
        setMaxDamage(0);
        this.names = names;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + getNames()[MathHelper.clamp_int(stack.getMetadata(), 0, getNames().length)];
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (int i = 0; i < getNames().length; i++) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }

    public String[] getNames() {
        return names;
    }
}
