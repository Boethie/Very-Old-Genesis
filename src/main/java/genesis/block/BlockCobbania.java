package genesis.block;

import java.util.List;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockCobbania extends BlockLilyPad
{
	public BlockCobbania()
	{
		setHardness(0.0F);
		setSoundType(SoundType.PLANT);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity entity)
	{}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Water;
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
	{
		return canBlockStay(world, pos, getDefaultState());
	}
}
