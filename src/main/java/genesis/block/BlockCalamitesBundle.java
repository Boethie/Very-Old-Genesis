package genesis.block;

import genesis.client.GenesisSounds;
import genesis.common.GenesisCreativeTabs;
import genesis.common.GenesisItems;
import genesis.util.Constants;
import net.minecraft.block.BlockHay;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockCalamitesBundle extends BlockHay
{
	public BlockCalamitesBundle()
	{
		setStepSound(GenesisSounds.CALAMITES);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		Blocks.fire.setFireInfo(this, 60, 20);
	}

	@Override
	public BlockCalamitesBundle setUnlocalizedName(String name)
	{
		super.setUnlocalizedName(Constants.PREFIX + name);

		return this;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 9;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return GenesisItems.calamites;
	}
}
