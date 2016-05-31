package genesis.world.biome.decorate;

import genesis.block.IMushroomBase;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenStemonitis extends WorldGenDecorationBase
{
	public WorldGenStemonitis()
	{
		super(WorldBlockMatcher.STANDARD_AIR, WorldBlockMatcher.TRUE);
	}
	
	@Override
	public boolean place(World world, Random random, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();

		if (block instanceof IMushroomBase)
		{
			IMushroomBase base = (IMushroomBase) block;
			IBlockState placedState = GenesisBlocks.stemonitis.getDefaultState();

			if (!base.canSustainMushroom(world, pos, EnumFacing.UP, placedState))
				return false;

			return setAirBlock(world, pos.up(), placedState);
		}
		
		return false;
	}
}
