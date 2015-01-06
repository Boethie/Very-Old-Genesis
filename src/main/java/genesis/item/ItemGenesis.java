package genesis.item;

import genesis.util.Constants;
import net.minecraft.item.Item;

public class ItemGenesis extends Item {
    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName(Constants.setUnlocalizedName(unlocalizedName));
    }
}
