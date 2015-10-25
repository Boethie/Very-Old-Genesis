package genesis.block.tileentity.portal;

import genesis.block.BlockGenesis;
import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumWorldBlockLayer;

public class BlockGenesisPortal extends BlockGenesis
{
	public BlockGenesisPortal()
	{
		super(Material.air);
		
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}
}
