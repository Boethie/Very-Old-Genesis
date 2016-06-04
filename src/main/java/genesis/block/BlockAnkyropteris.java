package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.common.sounds.GenesisSoundTypes;
import net.minecraft.block.BlockVine;

public class BlockAnkyropteris extends BlockVine
{
	public BlockAnkyropteris()
	{
		setHardness(0.2F);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setSoundType(GenesisSoundTypes.FERN);
	}
}
