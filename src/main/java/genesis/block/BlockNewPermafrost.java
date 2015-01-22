package genesis.block;

import genesis.common.GenesisItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockNewPermafrost extends BlockPermafrost {
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return getDrop(rand.nextInt(100));
    }

    private Item getDrop(int chance) {
        //if (chance < 3) return GenesisItems.necklace;
        if (chance < 5) return Items.arrow;
        else if (chance < 7) return Items.bow;
        else if (chance < 10) return Items.wooden_pickaxe;//pick
        else if (chance < 35) return Items.bone;
        else if (chance < 40) return GenesisItems.eryops_leg;//meat
        return null;
    }
}
