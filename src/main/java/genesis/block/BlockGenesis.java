package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockGenesis extends Block {
    public BlockGenesis(Material material) {
        super(material);
        setCreativeTab(GenesisCreativeTabs.BLOCK);
    }

    @Override
    public Block setUnlocalizedName(String name) {
        return super.setUnlocalizedName(Constants.PREFIX + name);
    }
}
