package genesis.block;

import genesis.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockGenesis extends Block {
    public BlockGenesis(Material material) {
        super(material);
    }

    @Override
    public Block setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName(Constants.setUnlocalizedName(unlocalizedName));
    }
}
