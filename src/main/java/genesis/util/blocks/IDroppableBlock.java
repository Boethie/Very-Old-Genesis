package genesis.util.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDroppableBlock
{
	boolean canStay(World world, BlockPos pos, IBlockState state);

	void setToAir(World world, BlockPos pos, IBlockState state);
}
