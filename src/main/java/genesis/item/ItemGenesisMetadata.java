package genesis.item;

import genesis.util.Metadata;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemGenesisMetadata extends ItemGenesis {
    private final Class metaClass;

    public ItemGenesisMetadata(Class metaClass) {
        this.metaClass = metaClass;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return Metadata.getUnlocalizedName(super.getUnlocalizedName(stack), stack.getMetadata(), getMetaClass());
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        Metadata.getSubItems(getMetaClass(), subItems);
    }

    public Class getMetaClass() {
        return metaClass;
    }
}
