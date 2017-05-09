package genesis.world.gen.feature;

import java.lang.reflect.InvocationTargetException;
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

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class WorldGenTreeDryophyllum extends WorldGenTreeBase
{
	private static final TreeTypes TYPE_SMALL_1 = TreeTypes.TYPE_1;
	private static final TreeTypes TYPE_SMALL_2 = TreeTypes.TYPE_3;
	private static final TreeTypes TYPE_BIG = TreeTypes.TYPE_2;
	
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
		int base = height - ((treeType == TYPE_SMALL_1 || treeType == TYPE_SMALL_2) ? 4 : 9);
		
		if (!BlockVolumeShape.region(-1, 1, -1, 1, base - 1, 1)
				.and(-3, base, -3, 3, height, 3)
				.hasSpace(pos, isEmptySpace(world)))
		{
			return false;
		}
		
		// target: ~8-10 and 10-15
		int trunkHeight = base;
		generateTrunk(world, pos, trunkHeight);
		if (treeType == TYPE_SMALL_1 || treeType == TYPE_SMALL_2)
		{
			generateSmallTreeLeaves(world, rand, pos.add(0, trunkHeight, 0));
		}
		else
		{
			generateBigTreeLeavesAndBranches(world, pos.add(0, trunkHeight, 0), rand);
		}
		return true;
	}
	
	private void generateBigTreeLeavesAndBranches(World world, BlockPos pos, Random rand)
	{
		for (int i = 0; i < 20; i++)
		{
			double r = 5.3;
			double yr = 9;
			double rPart = 2.7;
			Vec3d currentPos = new Vec3d(pos).add(
					new Vec3d(
							(rand.nextDouble() * 2 - 1) * r,
							(rand.nextDouble()) * yr,
							(rand.nextDouble() * 2 - 1) * r
					));
			generateLeaves(world, rand, currentPos, rPart);
			generateBranch(world, pos.down(rand.nextInt(5) + 2), currentPos);
		}
	}
	
	private void generateSmallTreeLeaves(World world, Random rand, BlockPos pos)
	{
		for (int i = 0; i < 8; i++)
		{
			double r = 2.3;
			double yr = 4;
			double rPart = 2.7;
			Vec3d currentPos = new Vec3d(pos).add(
					new Vec3d(
							(rand.nextDouble() * 2 - 1) * r,
							(rand.nextDouble()) * yr,
							(rand.nextDouble() * 2 - 1) * r
					));
			generateLeaves(world, rand, currentPos, rPart);
			generateBranch(world, pos.down(rand.nextInt(5) + 1), currentPos);
		}
	}
	
	private void generateBranch(World world, BlockPos trunkPos, Vec3d endPos)
	{
		Vec3d curr = new Vec3d(offsetHorizontallyTowards(trunkPos, endPos));
		Vec3d diff = endPos.subtract(curr);
		Vec3d dir = diff.normalize();
		
		while (curr.subtract(endPos).dotProduct(new Vec3d(1, 1, 1)) < 0)
		{
			BlockPos p = new BlockPos(curr);
			
			Vec3d next = next(curr, dir);
			if (!hasSpace(world, p, trunkPos) && hasNeighbor(world, new BlockPos(next)))
			{
				curr = next;
				continue;
			}
			IBlockState state = wood.withProperty(BlockLog.LOG_AXIS, getLogAxis(world, p));
			setBlockInWorld(world, p, state, true);
			curr = next;
		}
	}
	
	private BlockPos offsetHorizontallyTowards(BlockPos pos, Vec3d endPos)
	{
		double dx = endPos.xCoord - pos.getX();
		double dz = endPos.zCoord - pos.getZ();
		dx = Math.signum(dx);
		dz = Math.signum(dz);
		return pos.add(Math.round((float) dx), 0, Math.round((float) dz));
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
					if (dx == 0 && dy == 0 && dz == 0)
					{
						continue;
					}
					
					if (world.getBlockState(pos.add(dx, dy, dz)).getBlock() == wood.getBlock())
					{
						if (dx != 0)
						{
							weightX++;
						}
						if (dy != 0)
						{
							weightY++;
						}
						if (dz != 0)
						{
							weightZ++;
						}
					}
				}
			}
		}
		// when X wins over Z and Y doesn't win with X - use X
		if (weightX >= weightY && weightX > weightZ)
		{
			return BlockLog.EnumAxis.X;
		}
		// when horizontal is ambiguous and Y doesn't win
		if (weightX == weightZ && weightX >= weightY)
		{
			return BlockLog.EnumAxis.NONE;
		}
		// some of this is probably redundant, but check if Y wins
		if (weightY > weightZ && weightY > weightX)
		{
			return BlockLog.EnumAxis.Y;
		}
		if (weightZ >= weightY && weightZ > weightX)
		{
			return BlockLog.EnumAxis.Z;
		}
		return BlockLog.EnumAxis.NONE;
	}
	
	private boolean hasNeighbor(World world, BlockPos pos)
	{
		for (int dx = -1; dx <= 1; dx++)
		{
			for (int dy = -1; dy <= 1; dy++)
			{
				for (int dz = -1; dz <= 1; dz++)
				{
					if (dx == 0 && dy == 0 && dz == 0)
					{
						continue;
					}
					if (world.getBlockState(pos.add(dx, dy, dz)).getBlock() == wood.getBlock())
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean hasSpace(World world, BlockPos pos, BlockPos trunkPos)
	{
		int total = hasSpaceCount(world, pos.up(), trunkPos) +
				hasSpaceCount(world, pos.down(), trunkPos) +
				hasSpaceCount(world, pos.east(), trunkPos) +
				hasSpaceCount(world, pos.west(), trunkPos) +
				hasSpaceCount(world, pos.north(), trunkPos) +
				hasSpaceCount(world, pos.south(), trunkPos);
		return total < 2;
	}
	
	private int hasSpaceCount(World world, BlockPos pos, BlockPos trunk)
	{
		if (pos.getX() == trunk.getX() && pos.getZ() == trunk.getZ())
		{
			return 0;
		}
		return (world.getBlockState(pos.down()).getBlock() != wood.getBlock() ? 1 : 0);
	}
	
	private Vec3d next(Vec3d pos, Vec3d direction)
	{
		BlockPos origPos = new BlockPos(pos);
		do
		{
			pos = pos.add(direction);
		} while (origPos.equals(new BlockPos(pos)));
		return pos;
	}
	
	private void generateLeaves(World world, Random rand, Vec3d pos, double r)
	{
		double p = 1.6;
		for (int dx = -MathHelper.ceiling_double_int(r); dx <= MathHelper.ceiling_double_int(r); dx++)
		{
			for (int dy = -MathHelper.ceiling_double_int(r); dy <= MathHelper.ceiling_double_int(r); dy++)
			{
				for (int dz = -MathHelper.ceiling_double_int(r); dz <= MathHelper.ceiling_double_int(r); dz++)
				{
					// lower value of p makes it closer to diamond shape, higher value makes it closer to cube, value p=2 is perfect sphere.
					if (pow(abs(dx), p) + pow(abs(dy), p) + pow(abs(dz), p) - rand.nextFloat() * pow(r, p) <= pow(r, p))
					{
						setBlockInWorld(world, new BlockPos(pos.addVector(dx, dy, dz)), leaves);
					}
				}
			}
		}
	}
	
	
	private void generateTrunk(World world, BlockPos pos, int height)
	{
		for (int y = 0; y < height; y++)
		{
			world.setBlockState(pos.up(y), wood);
		}
	}
}
