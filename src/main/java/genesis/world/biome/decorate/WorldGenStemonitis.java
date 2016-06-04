package genesis.world.biome.decorate;

import java.util.Random;

import genesis.block.IMushroomBase;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenStemonitis extends WorldGenDecorationBase
{
	private static final IBlockState STATE = GenesisBlocks.stemonitis.getDefaultState();
	
	public WorldGenStemonitis()
	{
		super(WorldBlockMatcher.STANDARD_AIR,
				(state, world, pos) -> {
					if (state.getBlock() instanceof IMushroomBase)
						return ((IMushroomBase) state.getBlock()).canSustainMushroom(world, pos, EnumFacing.UP, STATE);
					return false;
				});
	}
	
	@Override
	public boolean place(World world, Random random, BlockPos pos)
	{
		return setAirBlock(world, pos, STATE);
	}
}
