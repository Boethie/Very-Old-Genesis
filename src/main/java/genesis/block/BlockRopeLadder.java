package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.SoundType;

public class BlockRopeLadder extends BlockLadder
{
	public BlockRopeLadder()
	{
		setHardness(0.4F);
		setSoundType(SoundType.LADDER);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setHarvestLevel("axe", 0);
	}
}
