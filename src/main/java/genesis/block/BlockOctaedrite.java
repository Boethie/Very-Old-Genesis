package genesis.block;

import genesis.common.GenesisItems;

public class BlockOctaedrite extends BlockGenesisRock
{
	public BlockOctaedrite()
	{
		super(1.0F, 10.0F, 1);
		setItemDropped(GenesisItems.octaedrite_shard);
		setQuantityDropped(4);
	}
}
