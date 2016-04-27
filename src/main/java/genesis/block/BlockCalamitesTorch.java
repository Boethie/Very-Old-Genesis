package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;

public class BlockCalamitesTorch extends BlockTorch
{
	public BlockCalamitesTorch()
	{
		super();
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
		setLightLevel(.9375F);
		setHardness(0.0F);
		setSoundType(SoundType.WOOD);
	}
}
