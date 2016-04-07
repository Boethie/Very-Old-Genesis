package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.random.i.IntRange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeLepidodendron extends WorldGenTreeBase
{
	public WorldGenTreeLepidodendron(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.LEPIDODENDRON, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand) - 5;
		
		if (!isCubeClear(world, pos.up(), 1, height))
		{
			return false;
		}
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(height);
		int leaves = 1 + rand.nextInt(2);
		
		doBranch(world, branchPos, 0, 0, rand, leaves, false);
		
		if (rand.nextInt(2) == 0)
		{
			doBranch(world, branchPos, 1, 0, rand, leaves, true);
			doBranch(world, branchPos, -1, 0, rand, leaves, true);
			doBranch(world, branchPos, 0, 1, rand, leaves, true);
			doBranch(world, branchPos, 0, -1, rand, leaves, true);
		}
		else
		{
			doBranch(world, branchPos, 1, 1, rand, leaves, true);
			doBranch(world, branchPos, -1, -1, rand, leaves, true);
			doBranch(world, branchPos, -1, 1, rand, leaves, true);
			doBranch(world, branchPos, 1, -1, rand, leaves, true);
		}
		
		doBranchLeaves(world, branchPos.down(), rand, false);
		
		return true;
	}
	
	private boolean doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength, boolean leaveBranch)
	{
		boolean generated = false;
		
		pos = pos.add((dirX), 0, (dirZ));
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, false, leaveLength);
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, false, leaveLength);
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, true, leaveLength);
		
		generated = true;
		
		return generated;
	}
	
	private void doBranchLeaves(World world, BlockPos pos, Random random, boolean cap)
	{
		doBranchLeaves(world, pos, random, cap, 1 + random.nextInt(2));
	}
}
