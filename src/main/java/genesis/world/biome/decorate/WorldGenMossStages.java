package genesis.world.biome.decorate;

import java.util.Random;

import com.google.common.base.Predicate;

import genesis.block.BlockMoss;
import genesis.common.GenesisBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenMossStages extends WorldGenDecorationBase
{
	protected final Predicate<IBlockState> soilPredicate;
	
	public WorldGenMossStages(Predicate<IBlockState> soilPredicate)
	{
		this.soilPredicate = soilPredicate;
	}
	
	public WorldGenMossStages()
	{
		this((s) -> s.getBlock() == Blocks.dirt || s.getBlock() == GenesisBlocks.moss);
	}
	
	@Override
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		do
		{
			IBlockState state = world.getBlockState(pos);
			
			if (!state.getBlock().isAir(state, world, pos) && !state.getBlock().isLeaves(state, world, pos))
				break;
			
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		boolean generated = false;
		
		if (setMoss(world, pos, random))
			generated = true;
		
		int mossCount = getPatchSize();
		
		for (int i = 0; i < mossCount; ++i)
			if (setMoss(world, pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3), random))
				generated = true;
		
		return generated;
	}
	
	private boolean setMoss(World world, BlockPos pos, Random rand)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (!soilPredicate.apply(state))
			return false;
		
		int stage = GenesisBlocks.moss.getTargetStage(GenesisBlocks.moss.getFertility(world, pos, true), rand);
		
		if (stage >= 0)
		{
			setBlockInWorld(world, pos, GenesisBlocks.moss.getDefaultState().withProperty(BlockMoss.STAGE, stage), true);
		}
		
		return true;
	}
}
