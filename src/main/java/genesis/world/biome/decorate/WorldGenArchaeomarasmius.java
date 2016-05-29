package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenArchaeomarasmius extends WorldGenDecorationBase
{
	public WorldGenArchaeomarasmius()
	{
		super(WorldBlockMatcher.STANDARD_AIR, WorldBlockMatcher.TRUE);
	}
	
	@Override
	public boolean place(World world, Random random, BlockPos pos)
	{
		IBlockState stateAt = world.getBlockState(pos);
		
		if ((stateAt.getBlock() != GenesisBlocks.moss && stateAt.getBlock() != Blocks.dirt)
				|| world.getLight(pos.up()) > 14)
			return false;
		
		setAirBlock(world, pos.up(), GenesisBlocks.archaeomarasmius.getDefaultState());
		
		return true;
	}
}
