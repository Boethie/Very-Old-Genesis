package genesis.world.gen.feature;

import genesis.combo.variant.EnumTree;
import genesis.util.random.i.IntRange;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenTreeCordaites extends WorldGenTreeBase
{
	private int leavesStart;
	
	public WorldGenTreeCordaites(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.CORDAITES, IntRange.create(minHeight, maxHeight), notify);
		
		leavesStart = minHeight - 5;
		
		setCanGrowInWater(true);
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		if (!isCubeClear(world, pos, 1, height))
			return false;
		
		IBlockState state = world.getBlockState(pos.up(4));
		
		if (state.getMaterial() != Material.AIR)
			return false;
		
		int baseHeight = 2 + rand.nextInt(2);
		
		for (int i = baseHeight; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos basePos = pos.up(baseHeight - 1);
		generateBaseBranch(world, basePos, rand, baseHeight, 1, 0);
		generateBaseBranch(world, basePos, rand, baseHeight, -1, 0);
		generateBaseBranch(world, basePos, rand, baseHeight, 0, 1);
		generateBaseBranch(world, basePos, rand, baseHeight, 0, -1);
		
		//TODO cordaites gen branchPos bug?
		BlockPos branchPos = pos.up(height - 1);
		
		int leavesLevel = Math.max(basePos.getY() + 2, pos.getY() + leavesStart);
		
		leavesLevel += rand.nextInt(3);
		
		//TODO: check if this should be relative (WRT to branchPos)
		branchPos = pos.up(height - 3);
		
		branchPos = branchPos.up(2);
		
		doPineTopLeaves(world, pos, branchPos, height, leavesLevel, rand, false, 3, false, false);
		branchPos = new BlockPos(pos.getX(), leavesLevel, pos.getZ());
		
		int leaves = 1 + rand.nextInt(2);
		
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
		
		generateResin(world, pos, height, baseHeight);
		
		return true;
	}
	
	private void generateBaseBranch(World world, BlockPos pos, Random rand, int baseHeight, int dirX, int dirZ)
	{
		int spread = 1;
		
		for (int i = 0; i < baseHeight; ++i)
		{
			if (i > 0 && rand.nextInt(2) == 0)
				++spread;
			
			setBlockInWorld(world, pos.add(spread * dirX, -i, spread * dirZ), wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE), true);
		}
	}
	
	private boolean doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength, boolean leaveBranch)
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
		
		pos = pos.add(0, 1, 0);
		setBlockInWorld(world, pos, (leaveBranch)? leaves : wood);
		doBranchLeaves(world, pos, random, true, leaveLength);
		
		return true;
	}
}
