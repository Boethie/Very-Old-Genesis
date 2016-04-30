package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockVine;
import net.minecraft.block.SoundType;

public class BlockAnkyropteris extends BlockVine
{
	public BlockAnkyropteris()
	{
		setHardness(0.2F);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setSoundType(SoundType.PLANT);
	}
}
