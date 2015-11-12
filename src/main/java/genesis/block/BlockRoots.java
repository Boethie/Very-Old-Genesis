package genesis.block;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Vorquel on 11/10/15.
 */
public class BlockRoots extends BlockGenesis
{
	public BlockRoots()
	{
		super(Material.vine);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean isVisuallyOpaque()
	{
		return false;
	}
}
