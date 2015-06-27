package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import genesis.util.Constants;

import java.util.Random;

import net.minecraft.block.BlockClay;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockRedClay extends BlockClay
{
	public BlockRedClay()
	{
		setHardness(0.6F);
		setStepSound(soundTypeGravel);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		setUnlocalizedName(Constants.PREFIX + "redClay");
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return GenesisItems.red_clay_ball;
	}
}
