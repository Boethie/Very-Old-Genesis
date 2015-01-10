package genesis.block;

import net.minecraft.block.material.Material;

public class BlockGenesisRock extends BlockGenesis {
    public BlockGenesisRock() {
        this(0);
    }

    public BlockGenesisRock(int harvestLevel) {
        super(Material.rock);
        setStepSound(soundTypePiston);
        setHarvestLevel("pickaxe", harvestLevel);
    }
}
