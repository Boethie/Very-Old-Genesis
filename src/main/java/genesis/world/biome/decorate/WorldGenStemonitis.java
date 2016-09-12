package genesis.world.biome.decorate;

import genesis.block.IMushroomBase;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenStemonitis extends WorldGenDecorationBase
{
	private static final IBlockState STATE = GenesisBlocks.STEMONITIS.getDefaultState();

	public WorldGenStemonitis()
	{
		super(WorldBlockMatcher.STANDARD_AIR,
				(state, world, pos) -> state.getBlock() instanceof IMushroomBase && ((IMushroomBase) state.getBlock()).canSustainMushroom(world, pos, EnumFacing.UP, STATE));
	}

	@Override
	public boolean place(World world, Random random, BlockPos pos)
	{
		return setAirBlock(world, pos, STATE);
	}
}
