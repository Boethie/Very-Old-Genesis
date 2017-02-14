package genesis.world.gen.feature;

import java.util.Random;

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
		
		int branchPairs = randomBranchPairCount(rand);
		
		for (int i = 0; i < branchPairs; ++i)
		{
			base = randomBaseHeight(rand);
			generateBranchPair(world, pos, rand, height, (base >= height - 2)? height - 5: base);
		}
		
		return true;
	}
	
	private int randomBranchPairCount(Random rand)
	{
		return treeType == TreeTypes.TYPE_1 || treeType == TreeTypes.TYPE_3? 1 + rand.nextInt(1): 2 + rand.nextInt(4);
	}
	
	private int randomBaseHeight(Random rand)
	{
		return treeType == TreeTypes.TYPE_1 || treeType == TreeTypes.TYPE_3? 1 + rand.nextInt(3): 2 + rand.nextInt(5);
	}
	
	private void generateBranchPair(World world, BlockPos pos, Random rand, int height, int base)
	{
		for (int y = 0; y < base; y++)
			world.setBlockState(pos.up(y), wood);
		
		int length = height - base;
		
		BlockPos startPos = pos.up(base);
		Vec3d startPosVec = new Vec3d(startPos);
		
		Vec3d branchDir = initialBranchDirection(rand);
		branchInDirection(world, rand, startPosVec, branchDir, length);
		
		branchDir = Vectors.randomizedMirroredXZ(rand, branchDir, new Vec3d(0.3, 0, 0.3));
		branchInDirection(world, rand, startPosVec, branchDir, length);
	}
	
	private Vec3d initialBranchDirection(Random rand)
	{
		return treeType == TreeTypes.TYPE_1 || treeType == TreeTypes.TYPE_3?
				Vectors.randomGaussianDirection(rand,
						new Vec3d(0, 5, 0), new Vec3d(3, 0, 3)):
				Vectors.randomGaussianDirection(rand,
						new Vec3d(0, 3, 0), new Vec3d(4, 0, 4));
	}
	
	private void branchInDirection(World world, Random rand, Vec3d start, Vec3d direction, int length)
	{
		length = MathHelper.ceiling_double_int(
				length / (1 +
						0.1 * Math.abs(direction.xCoord) + 0.1 * Math.abs(direction.zCoord))
		);
		
		int leavesStart = rand.nextInt(1) + 1;
		Vec3d currentPos = start;
		for (int i = 0; i < length; i++)
		{
			if (rand.nextBoolean())
			{
				direction = modifyDirection(rand, direction, i);
			}
			
			EnumFacing.Axis axis = Vectors.getMainAxis(direction);
			EnumAxis blockAxis = EnumAxis.fromFacingAxis(axis);
			BlockPos currentBlockPos = new BlockPos(currentPos);
			setBlockInWorld(world, currentBlockPos, wood.withProperty(BlockLog.LOG_AXIS, blockAxis));
			
			if (treeType != TreeTypes.TYPE_3 && i >= leavesStart)
			{
				int leavesSize = i == leavesStart? 1: 2;
				doBranchLeaves(world, currentBlockPos, rand, true, leavesSize, true);
			}
			
			currentPos = currentPos.add(direction);
		}
	}
	
	private Vec3d modifyDirection(Random rand, Vec3d direction, int i)
	{
		switch (treeType)
		{
		case TYPE_1:
		case TYPE_3:
			direction = direction.addVector(0, 0.4, 0);
			return Vectors.randomGaussianDirection(rand, direction, new Vec3d(0.2, 0, 0.2));
		default:
			direction = direction.addVector(0, 0.3, 0);
			return Vectors.randomGaussianDirection(rand, direction, new Vec3d(0.1, 0, 0.1));
		}
	}
}
