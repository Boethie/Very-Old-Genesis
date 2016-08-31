package genesis.world.biome.decorate;

import java.util.Random;

import genesis.block.BlockMoss;
import genesis.block.BlockMoss.EnumSoil;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenMossStages extends WorldGenDecorationBase
{
	public WorldGenMossStages()
	{
		super(WorldBlockMatcher.STANDARD_AIR, WorldBlockMatcher.TRUE);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		pos = pos.down();
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() == Blocks.DIRT)
			state = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.SOIL, EnumSoil.DIRT);
		else if (state.getBlock() == GenesisBlocks.humus)
			state = GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.SOIL, EnumSoil.HUMUS);
		else if (state.getBlock() != GenesisBlocks.moss)
			return false;
		
		int stage = GenesisBlocks.moss.getTargetStage(GenesisBlocks.moss.getFertility(world, pos, true), rand);
		
		if (stage >= 0)
			setBlock(world, pos, state.withProperty(BlockMoss.STAGE, stage));
		else
			setBlock(world, pos, state.getValue(BlockMoss.SOIL).getState());
		
		return true;
	}
}
