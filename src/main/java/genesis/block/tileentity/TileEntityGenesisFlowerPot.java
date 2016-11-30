package genesis.block.tileentity;

import genesis.common.GenesisBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityGenesisFlowerPot extends TileEntityFlowerPot
{
	public TileEntityGenesisFlowerPot() {}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return newState.getBlock() != GenesisBlocks.FLOWER_POT;
	}
}
