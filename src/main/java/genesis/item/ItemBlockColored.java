package genesis.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;

public class ItemBlockColored extends ItemColored {
    // hasSubtypes cannot be primitive due to FML registration
    public ItemBlockColored(Block block, Boolean hasSubtypes) {
        super(block, hasSubtypes);
    }
}
