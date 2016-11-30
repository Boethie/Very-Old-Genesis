package genesis.world.gen.feature;

import genesis.combo.variant.EnumTree;
import genesis.util.WorldUtils;
import genesis.util.functional.WorldBlockMatcher;
import genesis.util.random.i.IntRange;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeSigillaria extends WorldGenTreeBase
{
	public WorldGenTreeSigillaria(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.SIGILLARIA, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	public int getRadius()
	{
		return 4;
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand) - 5;
		
		if (WorldUtils.isMatchInCylinder(world, pos.up(), WorldBlockMatcher.not(WorldBlockMatcher.STANDARD_AIR), 1, 1, height))
			return false;
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(height);
		int leaves = 1;
		
		if (rand.nextInt(8) > 4)
		{
			doBranch(world, branchPos, 0, 0, rand, leaves, true);
		}
		else if (rand.nextInt(2) == 0)
		{
			doBranch(world, branchPos, 1, 0, rand, leaves);
			doBranch(world, branchPos, -1, 0, rand, leaves);
		}
		else
		{
			doBranch(world, branchPos, 0, 1, rand, leaves);
			doBranch(world, branchPos, 0, -1, rand, leaves);
		}
		
		return true;
	}
	
	private void doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength)
	{
		doBranch(world, pos, dirX, dirZ, random, leaveLength, false);
	}
	
	private void doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength, boolean forceLongBranch)
	{
		pos = pos.add((dirX), 0, (dirZ));
		setBlockInWorld(world, pos, wood);
		
		pos = pos.add((dirX), 1, (dirZ));
		setBlockInWorld(world, pos, wood);
		
		if (random.nextInt(2) == 0 || forceLongBranch)
		{
			pos = pos.add(0, 1, 0);
			setBlockInWorld(world, pos, wood);
			doBranchLeaves(world, pos, random, false, leaveLength);
		}
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, wood);
		doBranchLeaves(world, pos, random, true, leaveLength);
	}
}
