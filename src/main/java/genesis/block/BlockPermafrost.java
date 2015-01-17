package genesis.block;

import net.minecraft.block.material.Material;

public class BlockPermafrost extends BlockGenesis {
    public BlockPermafrost() {
        super(Material.rock);
        slipperiness = 0.98F;
        setHarvestLevel("pickaxe", 0);
    }
}
