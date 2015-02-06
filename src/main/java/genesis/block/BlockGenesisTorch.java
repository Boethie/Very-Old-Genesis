package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants;
import net.minecraft.block.BlockTorch;

public class BlockGenesisTorch extends BlockTorch
{

	public BlockGenesisTorch()
	{
		super();
		this.setCreativeTab(GenesisCreativeTabs.DECORATIONS).setLightLevel(.9375F).setHardness(0.0F).setStepSound(soundTypeWood);
	}

	@Override
	public BlockGenesisTorch setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(Constants.PREFIX + name);

		return this;
	}

}
