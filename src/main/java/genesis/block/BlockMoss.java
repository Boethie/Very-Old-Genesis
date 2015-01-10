package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.BlockState;

public class BlockMoss extends BlockGrass {
    public BlockMoss() {
        super();
        setCreativeTab(GenesisCreativeTabs.BLOCK);
        setHarvestLevel("shovel", 0);
    }

    @Override
    public Block setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName(Constants.setUnlocalizedName(unlocalizedName));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SNOWY);
    }
}
