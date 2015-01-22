package genesis.block;

import genesis.common.GenesisItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockOctaedrite extends BlockGenesisRock
{
	public BlockOctaedrite()
	{
		super(1);
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return GenesisItems.octaedrite_shard;
	}

	public int quantityDropped(Random random)
	{
		return 4;
	}
}
