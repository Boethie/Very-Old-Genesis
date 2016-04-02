package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.sounds.GenesisSoundTypes;
import net.minecraft.block.BlockHay;
import net.minecraft.init.Blocks;

public class BlockCalamitesBundle extends BlockHay
{
	public BlockCalamitesBundle()
	{
		setSoundType(GenesisSoundTypes.CALAMITES);
		setCreativeTab(GenesisCreativeTabs.BLOCK);

		setHardness(1);
		setHarvestLevel("axe", 0);

		Blocks.fire.setFireInfo(this, 30, 5);
	}
}
