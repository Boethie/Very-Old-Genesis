package genesis.world.gen.feature;

import java.util.Random;

import com.google.common.base.Predicates;

import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.i.IntRange;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeVoltzia extends WorldGenTreeBase
{
	public WorldGenTreeVoltzia (int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.VOLTZIA, IntRange.create(minHeight, maxHeight), notify);
		
		setSoilPredicate(Predicates.and(BlockStateMatcher.forBlock(GenesisBlocks.moss),
				BlockStateMatcher.forBlock(Blocks.dirt)
						.where(BlockDirt.VARIANT, (v) -> v == BlockDirt.DirtType.DIRT || v == BlockDirt.DirtType.COARSE_DIRT)));
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		if (!isCubeClear(world, pos, 1, height))
			return false;
		
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(height - 2);
		
		doPineTopLeaves(world, pos, branchPos, height, pos.getY() + 1, rand, false, 2, true, false);
		
		return true;
	}
}
