package genesis.block;

import java.util.List;

import genesis.common.GenesisCreativeTabs;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockCobbania extends BlockLilyPad
{
	public BlockCobbania()
	{
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setCreativeTab(GenesisCreativeTabs.DECORATIONS);
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Water;
	}
}
