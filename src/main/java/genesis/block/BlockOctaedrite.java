package genesis.block;

import genesis.common.GenesisItems;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockOctaedrite extends BlockGenesisRock
{
	public BlockOctaedrite()
	{
		super(1.0F, 10.0F, 1);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return GenesisItems.octaedrite_shard;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 4;
	}
}
