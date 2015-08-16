package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockGenesis extends Block
{
	public BlockGenesis(Material material)
	{
		super(material);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}
}
