package genesis.world.gen.feature;

import genesis.combo.variant.EnumTree;
import genesis.util.WorldUtils;
import genesis.util.functional.WorldBlockMatcher;
import genesis.util.random.i.IntRange;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreePsaronius extends WorldGenTreeBase
{
	public WorldGenTreePsaronius(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.PSARONIUS, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	public int getRadius()
	{
		return 4;
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand) - 1;
		
		if (WorldUtils.isMatchInCylinder(world, pos.up(), WorldBlockMatcher.not(WorldBlockMatcher.STANDARD_AIR), 1, 1, height))
			return false;
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(height);
		
		setBlockInWorld(world, branchPos, leaves);
		
		doLeavesBranch(world, branchPos, 1, 0, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, -1, 0, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, 0, 1, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, 0, -1, rand, 2 + rand.nextInt(3));
		
		doLeavesBranch(world, branchPos, 1, 1, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, -1, 1, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, 1, -1, rand, 2 + rand.nextInt(3));
		doLeavesBranch(world, branchPos, -1, -1, rand, 2 + rand.nextInt(3));
		
		return true;
	}
	
	private void doLeavesBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int length)
	{
		for (int i = 1; i <= length; ++ i)
		{
			pos = pos.add((dirX), 0, (dirZ));
			
			if (i == length)
				pos = pos.add(0, -1, 0);
			
			setBlockInWorld(world, pos, leaves);
		}
	}
}
