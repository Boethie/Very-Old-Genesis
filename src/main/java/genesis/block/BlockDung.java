package genesis.block;

import genesis.common.GenesisItems;
import net.minecraft.block.material.Material;

public class BlockDung extends BlockGenesis
{
	public BlockDung()
	{
		super(Material.wood);
		//setItemDropped(GenesisItems.dung);
		setQuantityDropped(4);
	}
}
