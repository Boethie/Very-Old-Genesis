package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.sounds.GenesisSoundTypes;
import net.minecraft.block.material.Material;

public class BlockOoze extends BlockGenesis
{
	public BlockOoze()
	{
		super(Material.clay, GenesisSoundTypes.OOZE);
		
		slipperiness = 0.88F;
		setSoundType(GenesisSoundTypes.OOZE);
		setHarvestLevel("shovel", 0);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setHardness(1.0F);
	}
}
