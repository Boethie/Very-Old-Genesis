package genesis.block;

import genesis.common.GenesisCreativeTabs;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGenesisStairs extends BlockStairs
{
	protected final IBlockState modelState;
	
	public BlockGenesisStairs(IBlockState modelState)
	{
		super(modelState);
		
		this.modelState = modelState;
		
		setCreativeTab(GenesisCreativeTabs.BLOCK);
		useNeighborBrightness = true;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return modelState.getBlock().getFlammability(world, pos, face);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return modelState.getBlock().getFireSpreadSpeed(world, pos, face);
	}
}
