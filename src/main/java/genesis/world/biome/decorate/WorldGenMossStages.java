package genesis.world.biome.decorate;

import java.util.Random;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenMossStages extends WorldGenDecorationBase
{
	public WorldGenMossStages()
	{
		super(WorldBlockMatcher.STANDARD_AIR, (s, w, p) -> s.getBlock() == Blocks.dirt || s.getBlock() == GenesisBlocks.moss);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		pos = pos.down();
		int stage = GenesisBlocks.moss.getTargetStage(GenesisBlocks.moss.getFertility(world, pos, true), rand);
		return setBlockInWorld(world, pos,
				stage >= 0 ? GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, stage) : Blocks.dirt.getDefaultState(),
				true);
	}
}
