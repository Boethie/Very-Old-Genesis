package genesis.block;

import genesis.client.GenesisSounds;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.material.Material;

public class BlockOoze extends BlockGenesis
{
	public BlockOoze()
	{
		super(Material.clay);
		slipperiness = 0.88F;
		setStepSound(GenesisSounds.OOZE);
		setHarvestLevel("shovel", 0);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setHardness(1.0F);
		setUnlocalizedName("ooze");
	}
}
