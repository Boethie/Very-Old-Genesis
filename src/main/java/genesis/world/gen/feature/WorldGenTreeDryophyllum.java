package genesis.world.gen.feature;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.BlockVolumeShape;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.WeightedIntItem;
import genesis.util.random.i.WeightedIntProvider;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Random;

import static genesis.util.WorldUtils.toBlockPos;
import static genesis.util.WorldUtils.toVec3d;
import static genesis.util.WorldUtils.xzEqual;
import static java.lang.Math.*;
import static net.minecraft.util.math.MathHelper.getRandomIntegerInRange;

public class WorldGenTreeDryophyllum extends WorldGenTreeBase
{
	// tweak parameters
	// distanceExpConstant changes how close to sphere, cube or diamond shape leave generation is.
	// for small radius smaller value is recommended, perfect spheres look too cube-like
	// (0, 1)   => diamond shape bent inwards
	// [1, 1]   => diamond shape
	// (1, 2)   => between sphere and diamond shape
	// [2, 2]   => sphere
	// [2, inf] => between sphere and cube
	// there is no value that generates perfect cubes
	// branchSourceRelativeMinY is relative to the top of the trunk
	private static final int branchTargetsAmountSmall = 8;
	private static final int branchSourceRelativeMinYSmall = -3;
	private static final int branchSourceRelativeMaxYSmall = -1;
	private static final double leavesRadiusSmall = 2.2;
	private static final double xzBranchTargetSpreadSmall = 2;
	private static final double yBranchTargetSpreadSmall = 2.2;
	private static final double distanceExpConstantSmall = 1.6;
	private static final double leavesShapeRandomizationSmall = 1.0;
	private static final int leavesHeightTotalSmall = MathHelper.ceiling_double_int(leavesRadiusSmall + yBranchTargetSpreadSmall * 2);
	
	private static final int branchTargetsAmountBig = 15;
	private static final int branchSourceRelativeMinYBig = -4;
	private static final int branchSourceRelativeMaxYBig = -1;
	private static final double leavesRadiusBig = 2.2;
	private static final double xzBranchTargetSpreadBig = 4.3;
	private static final double yBranchTargetSpreadBig = 3.5;
	private static final double distanceExpConstantBig = 1.6;
	private static final double leavesShapeRandomizationBig = 1.0;
	private static final int leavesHeightTotalBig = MathHelper.ceiling_double_int(leavesRadiusBig + yBranchTargetSpreadBig * 2);
	
	private static final TreeTypes TYPE_SMALL_1 = TreeTypes.TYPE_1;
	private static final TreeTypes TYPE_SMALL_2 = TreeTypes.TYPE_3;
	
	public WorldGenTreeDryophyllum(int minHeight, int maxHeight, boolean notify, IBlockState wood)
	{
		super(
				GenesisBlocks.TREES.getBlockState(TreeBlocksAndItems.SAPLING, EnumTree.DRYOPHYLLUM),
				wood,
				null,
				null,
				IntRange.create(minHeight, maxHeight),
				notify);
	}
	
	public WorldGenTreeDryophyllum(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.DRYOPHYLLUM, IntRange.create(minHeight, maxHeight), notify);
		
		this.saplingCountProvider = new WeightedIntProvider(
				WeightedIntItem.of(104, 0),
				WeightedIntItem.of(6, IntRange.create(1, 3)));
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		int trunkHeight = height - ((treeType == TYPE_SMALL_1 || treeType == TYPE_SMALL_2) ? leavesHeightTotalSmall : leavesHeightTotalBig);
		
		if (!BlockVolumeShape.region(-1, 1, -1, 1, trunkHeight - 1, 1)
				.and(-3, trunkHeight, -3, 3, height, 3)
				.hasSpace(pos, isEmptySpace(world)))
			return false;
		
		generateTrunk(world, pos, trunkHeight);
		if (treeType == TYPE_SMALL_1 || treeType == TYPE_SMALL_2)
			generateSmallTreeLeaves(world, rand, pos.add(0, trunkHeight, 0));
		else
			generateBigTreeLeavesAndBranches(world, pos.add(0, trunkHeight, 0), rand);
		return true;
	}
	
	private void generateBigTreeLeavesAndBranches(World world, BlockPos pos, Random rand)
	{
		for (int i = 0; i < branchTargetsAmountBig; i++)
		{
			double r = xzBranchTargetSpreadBig;
			double yr = yBranchTargetSpreadBig * 2;
			Vec3d currentPos = toVec3d(pos).add(new Vec3d(
					(rand.nextDouble() * 2 - 1) * r, (rand.nextDouble()) * yr, (rand.nextDouble() * 2 - 1) * r
			));
			BlockPos tryPos = pos.up(getRandomIntegerInRange(rand, branchSourceRelativeMinYBig, branchSourceRelativeMaxYBig));
			double dy = currentPos.yCoord - pos.getY();
			if (abs(dy) > yr)
				tryPos = tryPos.up((int) round(abs(dy) - yr));
			
			generateLeaves(world, rand, currentPos, leavesRadiusBig, distanceExpConstantBig, leavesShapeRandomizationBig);
			generateBranch(world, rand, tryPos, currentPos, true);
		}
	}
	
	private void generateSmallTreeLeaves(World world, Random rand, BlockPos pos)
	{
		for (int i = 0; i < branchTargetsAmountSmall; i++)
		{
			double r = xzBranchTargetSpreadSmall;
			double yr = yBranchTargetSpreadSmall * 2;
			Vec3d currentPos = toVec3d(pos).add(new Vec3d(
					(rand.nextDouble() * 2 - 1) * r, (rand.nextDouble()) * yr, (rand.nextDouble() * 2 - 1) * r
			));
			BlockPos branchSrc = pos.up(getRandomIntegerInRange(rand, branchSourceRelativeMinYSmall, branchSourceRelativeMaxYSmall));
			generateLeaves(world, rand, currentPos, leavesRadiusSmall, distanceExpConstantSmall, leavesShapeRandomizationSmall);
			generateBranch(world, rand, branchSrc, currentPos, false);
		}
	}
	
	private void generateBranch(World world, Random rand, BlockPos trunkPos, Vec3d endPos, boolean avoidLongBranches)
	{
		Vec3d curr = toVec3d(trunkPos);
		
		BlockPos prev = null;
		while (endPos.squareDistanceTo(curr) > sqrt(3))
		{
			BlockPos currBlock = toBlockPos(curr);
			Vec3d dir = endPos.subtract(curr).normalize();
			Vec3d next = next(world, curr, dir, endPos, trunkPos);
			
			IBlockState state = wood.withProperty(BlockLog.LOG_AXIS, getLogAxis(world, currBlock));
			setBlockInWorld(world, currBlock, state, true);
			
			// check to avoid long straight up branches
			BlockPos nextBlock = toBlockPos(next);
			if (avoidLongBranches && prev != null && xzEqual(prev, currBlock) && xzEqual(currBlock, nextBlock))
			{
				prev = currBlock;
				curr = next.addVector(rand.nextBoolean() ? -1 : 1, 0, rand.nextBoolean() ? -1 : 1);
			}
			else
			{
				prev = currBlock;
				curr = next;
			}
		}
	}
	
	private BlockLog.EnumAxis getLogAxis(World world, BlockPos pos)
	{
		// this finds the right direction based on neighbors
		int weightX = 0, weightY = 0, weightZ = 0;
		for (int dx = -1; dx <= 1; dx++)
		{
			for (int dy = -1; dy <= 1; dy++)
			{
				for (int dz = -1; dz <= 1; dz++)
				{
					if (dx != 0 || dy != 0 || dz != 0)
					{
						if (world.getBlockState(pos.add(dx, dy, dz)).getBlock() == wood.getBlock())
						{
							weightX += dx == 0 ? 0 : 1;
							weightY += dy == 0 ? 0 : 1;
							weightZ += dz == 0 ? 0 : 1;
						}
					}
				}
			}
		}
		// when X wins over Z and Y doesn't win with X - use X
		if (weightX >= weightY && weightX > weightZ)
			return BlockLog.EnumAxis.X;
		// when horizontal is ambiguous and Y doesn't win
		if (weightX == weightZ && weightX >= weightY)
			return BlockLog.EnumAxis.NONE;
		// some of this is probably redundant, but check if Y wins
		if (weightY > weightZ && weightY > weightX)
			return BlockLog.EnumAxis.Y;
		if (weightZ >= weightY && weightZ > weightX)
			return BlockLog.EnumAxis.Z;
		return BlockLog.EnumAxis.NONE;
	}
	
	private Vec3d next(World world, Vec3d previousPos, Vec3d direction, Vec3d target, Vec3i trunkSrcPos)
	{
		Vec3d bestPos = null;
		double bestDist = previousPos.squareDistanceTo(target) - 0.5;
		
		Vec3d bestNoTrunkPos = null;
		double bestNoTrunkDist = previousPos.squareDistanceTo(target) - 0.5;
		for (int dx = -1; dx <= 1; dx++)
		{
			for (int dz = -1; dz <= 1; dz++)
			{
				// try to find point closer to target that isn't at trunk XZ coords.
				BlockPos blockPosXZ = toBlockPos(previousPos.addVector(dx, 0, dz));
				if (xzEqual(trunkSrcPos, blockPosXZ))
				{
					Vec3d newPos = toVec3d(blockPosXZ);
					double dist = newPos.squareDistanceTo(target);
					if (dist < bestNoTrunkDist)
					{
						bestNoTrunkPos = newPos;
						bestNoTrunkDist = dist;
					}
				}
				for (int dy = -1; dy <= 1; dy++)
				{
					if (dx != 0 || dy != 0 || dz != 0)
					{
						// find surrounding existing blocks that aren't trunk
						BlockPos blockPos = toBlockPos(previousPos.addVector(dx, dy, dz));
						if (!xzEqual(trunkSrcPos, blockPos) && world.getBlockState(blockPos).getBlock() == wood.getBlock())
						{
							Vec3d newPos = toVec3d(blockPos);
							double dist = newPos.squareDistanceTo(target);
							if (dist < bestDist)
							{
								bestPos = newPos;
								bestDist = dist;
							}
						}
					}
				}
			}
		}
		Vec3d nextDirect = previousPos;
		BlockPos origPos = toBlockPos(previousPos);
		do
			nextDirect = nextDirect.add(direction);
		while (origPos.equals(toBlockPos(nextDirect)));
		
		if (bestPos != null)
		{
			Vec3d trunkVec = toVec3d(trunkSrcPos);
			double diff = 1 + previousPos.subtract(trunkVec).normalize().dotProduct(target.subtract(previousPos).normalize());
			double distDirect = nextDirect.squareDistanceTo(target);
			double distBest = bestPos.squareDistanceTo(target);
			if (distBest < distDirect + diff)
				return bestPos;
		}
		if (bestNoTrunkPos != null)
			return bestNoTrunkPos;
		return nextDirect;
	}
	
	private void generateLeaves(World world, Random rand, Vec3d pos, double r, double expConst, double randomization)
	{
		for (int dx = -MathHelper.ceiling_double_int(r); dx <= MathHelper.ceiling_double_int(r); dx++)
		{
			for (int dy = -MathHelper.ceiling_double_int(r); dy <= MathHelper.ceiling_double_int(r); dy++)
			{
				for (int dz = -MathHelper.ceiling_double_int(r); dz <= MathHelper.ceiling_double_int(r); dz++)
				{
					// lower value of p makes it closer to diamond shape, higher value makes it closer to cube, value p=2 is perfect sphere.
					if (pow(abs(dx), expConst) + pow(abs(dy), expConst) + pow(abs(dz), expConst)
							- rand.nextFloat() * randomization * pow(r, expConst) <= pow(r, expConst))
						setBlockInWorld(world, toBlockPos(pos.addVector(dx, dy, dz)), leaves);
				}
			}
		}
	}
	
	private void generateTrunk(World world, BlockPos pos, int height)
	{
		for (int y = 0; y < height; y++)
			world.setBlockState(pos.up(y), wood);
	}
}
