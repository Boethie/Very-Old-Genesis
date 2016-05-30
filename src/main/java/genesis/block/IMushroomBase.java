package genesis.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IMushroomBase
{
	boolean canSustainMushroom(IBlockAccess world, BlockPos pos, EnumFacing side, IBlockState mushroomState);
}
