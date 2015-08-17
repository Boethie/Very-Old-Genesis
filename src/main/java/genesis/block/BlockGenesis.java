package genesis.block;

import genesis.common.GenesisCreativeTabs;
import genesis.util.Constants.Unlocalized;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockGenesis extends Block
{
	public BlockGenesis(Material material)
	{
		super(material);
		setCreativeTab(GenesisCreativeTabs.BLOCK);
	}
}
