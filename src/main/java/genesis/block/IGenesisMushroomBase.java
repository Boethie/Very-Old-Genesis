package genesis.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IGenesisMushroomBase
{
	boolean canSustainMushroom(IBlockAccess world, BlockPos pos, IBlockState state);
}
