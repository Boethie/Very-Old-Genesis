package genesis.item;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.item.Item;

public class ItemGenesis extends Item {
    public ItemGenesis() {
        setCreativeTab(GenesisCreativeTabs.MATERIALS);
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName(Constants.PREFIX + unlocalizedName);
    }
}
