package genesis.world.gen.feature;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.BlockVolumeShape;
import genesis.util.math.Vectors;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.WeightedIntItem;
import genesis.util.random.i.WeightedIntProvider;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldGenTreeDryophyllum extends WorldGenTreeBase
{
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
		int base = 4 + rand.nextInt(4);
		
		if (!BlockVolumeShape.region(-1, 1, -1, 1, base - 1, 1)
					 .and(-3, base, -3, 3, base + height, 3)
					 .hasSpace(pos, isEmptySpace(world)))
			return false;
		
		int branches = randomBranchCount(rand);
		
		int minBaseY, maxBaseY;
		if (treeType == TreeTypes.TYPE_1 || treeType == TreeTypes.TYPE_3)
		{
			minBaseY = 1;
			maxBaseY = 3;
		}
		else
		{
			minBaseY = 2;
			maxBaseY = 6;
		}
		generateBranches(world, pos, rand, minBaseY, maxBaseY, height, branches);
		return true;
	}
	
	private int randomBranchCount(Random rand)
	{
		return treeType == TreeTypes.TYPE_1 || treeType == TreeTypes.TYPE_3? 2 + rand.nextInt(2): 4 + rand.nextInt(8);
	}
	
	private void generateBranches(World world, BlockPos pos, Random rand, int minBase, int maxBase, int height, int count)
	{
		for (int y = 0; y < maxBase; y++)
			world.setBlockState(pos.up(y), wood);
		
		int minLeavesStartY = treeType == TreeTypes.TYPE_2 ? 2 : 1;
		int maxLeavesStartY = treeType == TreeTypes.TYPE_2 ? 5 : 3;
		
		Iterable<Vec3d> branches = branchDirections(rand, count);
		for (Vec3d direction : branches)
		{
			int base = MathHelper.getRandomIntegerInRange(rand, minBase, maxBase);
			int length = lengthForDirection(direction, height - base);
			
			int splitPoint = getSplitPoint(rand, length);
			
			int leavesStart = MathHelper.getRandomIntegerInRange(rand, minLeavesStartY, maxLeavesStartY);
			Predicate<BlockPos> genLeaves = p ->
					(treeType != TreeTypes.TYPE_3) && (p.getY() >= pos.getY() + leavesStart);
			branchInDirection(world, rand, new Vec3d(pos.up(base)), direction, length, splitPoint, genLeaves);
		}
	}
	
	private Iterable<Vec3d> branchDirections(Random rand, int count)
	{
		if (count == 1)
		{
			return Sets.newHashSet(Vectors.UP);
		}
		final double randomFactor = 0.3;
		
		Set<Vec3d> directions = new HashSet<>();
		while (directions.size() < count)
		{
			int toGenerate = count - directions.size();
			if (toGenerate % 2 == 0 && toGenerate > 2 && directions.isEmpty())
			{
				// if there are more than 2 even number to generate - generate one that goes almost up first
				Vec3d vec = Vectors.randomGaussianDirection(rand, Vectors.UP, new Vec3d(0.1, 0, 0.1));
				directions.add(vec);
				continue;
			}
			double randomRotation = rand.nextDouble() * Math.PI * 2;
			Vec3d[] vecs;
			if (toGenerate % 2 == 0)
			{
				vecs = new Vec3d[]{new Vec3d(2, 2, 0), new Vec3d(0, 2, 2)};
			}
			else
			{
				vecs = new Vec3d[]{new Vec3d(0, 2, Math.sqrt(2)), new Vec3d(1, 2, -1), new Vec3d(-1, 2, -1)};
			}
			for (int i = 0; i < vecs.length; i++)
			{
				// randomize direction
				vecs[i] = Vectors.randomGaussianDirection(rand, vecs[i], new Vec3d(randomFactor, randomFactor, randomFactor));
				// and rotate by random angle around Y axis
				vecs[i] = Vectors.rotateAroundAxis(EnumFacing.Axis.Y, vecs[i], randomRotation);
				directions.add(vecs[i]);
			}
		}
		return directions;
	}
	
	private void branchInDirection(World world, Random rand, Vec3d start, Vec3d direction, int length, int splitPoint, Predicate<BlockPos> generateLeaves)
	{
		Vec3d currentPos = start;
		for (int i = 0; i < length; i++)
		{
			if (i == splitPoint)
			{
				final double r = 0.7;
				Vec3d v1;
				do
				{
					v1 = Vectors.randomDirection(rand, direction, new Vec3d(r, r, r));
				} while (v1.dotProduct(direction) > 0.97);
				
				Vec3d v2;
				do
				{
					v2 = Vectors.randomDirection(rand, direction, new Vec3d(r, r, r));
				} while (v1.dotProduct(direction) > 0.97 || v1.dotProduct(v2) > 0.95);
				int lenLeft = length - i - 1;
				branchInDirection(world, rand, currentPos, v1, lenLeft, getSplitPoint(rand, length), generateLeaves);
				branchInDirection(world, rand, currentPos, v2, lenLeft, getSplitPoint(rand, length), generateLeaves);
				break;
			}
			if (rand.nextBoolean())
			{
				direction = modifyDirection(rand, direction);
			}
			
			EnumFacing.Axis axis = Vectors.getMainAxis(direction);
			EnumAxis blockAxis = EnumAxis.fromFacingAxis(axis);
			BlockPos currentBlockPos = new BlockPos(currentPos);
			setBlockInWorld(world, currentBlockPos, wood.withProperty(BlockLog.LOG_AXIS, blockAxis));
			
			if (generateLeaves.apply(currentBlockPos))
			{
				int leavesSize = rand.nextBoolean() ? 2 : 3;
				doBranchLeaves(world, currentBlockPos, rand, true, leavesSize, rand.nextBoolean());
			}
			
			currentPos = currentPos.add(Vectors.randomGaussianDirection(rand, direction, new Vec3d(0.2, 0.2, 0.2)));
		}
	}
	
	private int lengthForDirection(Vec3d direction, int length)
	{
		return Math.max(4, MathHelper.ceiling_double_int(length * direction.dotProduct(Vectors.UP)));
	}
	
	private Vec3d modifyDirection(Random rand, Vec3d direction)
	{
		switch (treeType)
		{
		case TYPE_1:
		case TYPE_3:
			// small
			direction = direction.addVector(0, 0.4, 0);
			return Vectors.randomGaussianDirection(rand, direction, new Vec3d(0.15, 0, 0.15));
		default:
			// big
			direction = direction.addVector(0, 0.3, 0);
			return Vectors.randomGaussianDirection(rand, direction, new Vec3d(0.1, 0, 0.1));
		}
	}
	
	private int getSplitPoint(Random rand, int length)
	{
		final int minBeforeSplit = 1;
		final int minAfterSplit = 2;
		final int minForSplit = minAfterSplit + minBeforeSplit;
		return length > minForSplit
				? MathHelper.getRandomIntegerInRange(rand, 2, length - 3)
				: -1;
	}
}
