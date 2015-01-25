package genesis.block;

import genesis.common.GenesisItems;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockDung extends BlockGenesis
{
	public BlockDung()
	{
		super(Material.rock);
		setItemDropped(GenesisItems.dung);
		setQuantityDropped(4);
		Blocks.fire.setFireInfo(this, 5, 5);
	}
}
