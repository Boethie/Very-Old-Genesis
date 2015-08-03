package genesis.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IGenesisMushroomBase
{
	boolean canSustainMushroom(IBlockAccess world, BlockPos pos, IBlockState state);
}
