package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import net.minecraft.block.BlockClay;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockRedClay extends BlockClay {
    public BlockRedClay() {
        setCreativeTab(GenesisCreativeTabs.BLOCK);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return GenesisItems.red_clay_ball;
    }
}
