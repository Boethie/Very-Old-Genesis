package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockMycelium;

public class BlockPrototaxitesMycelium extends BlockMycelium
{
	public BlockPrototaxitesMycelium() {
		setHardness(0.6F);
		setStepSound(soundTypeGrass);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}
}
