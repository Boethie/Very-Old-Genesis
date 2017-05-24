package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.WorldUtils;
import genesis.util.functional.WorldBlockMatcher;
import genesis.util.random.i.IntRange;
import net.minecraft.util.EnumFacing;
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
		
		if (WorldUtils.isMatchInCylinder(world, pos, WorldBlockMatcher.not(WorldBlockMatcher.STANDARD_AIR), 2, 2, height))
			return false;
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(height);
		int leaves = 3 + rand.nextInt(2);
		int lStep = 0;
		
		for (int li = 2; li <= leaves; ++ li)
		{
			this.generateLeafLayerCircle(world, rand, li, branchPos.add(0, lStep, 0));
			for (EnumFacing f : EnumFacing.HORIZONTALS)
				if (rand.nextInt(3 + (4 - li)) == 0)
				{
					if (li == 2)
						this.generateLeafLayerCircle(world, rand, 1, branchPos.add(0, 1, 0));
					
					this.generateLeafLayerCircle(world, rand, li, branchPos.offset(f).add(0, lStep, 0));
				}
			--lStep;
		}
		
		this.generateLeafLayerCircle(world, rand, 2, branchPos.add(0, lStep, 0));
		
		return true;
	}
	
	@SuppressWarnings("unused")
	private void doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength, boolean leaveBranch)
	{
		pos = pos.add((dirX), 0, (dirZ));
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, false, leaveLength);
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, false, leaveLength);
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, true, leaveLength);
	}
	
	@SuppressWarnings("unused")
	private void doBranchLeaves(World world, BlockPos pos, Random random, boolean cap)
	{
		doBranchLeaves(world, pos, random, cap, 1 + random.nextInt(2));
	}
}
