package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.BlockVolumeShape;
import genesis.util.random.i.IntRange;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class WorldGenTreeGinkgo extends WorldGenTreeBase
{
	public WorldGenTreeGinkgo(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.GINKGO, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		int base = 2 + rand.nextInt(4);
		
		if (!BlockVolumeShape.region(-1, 1, -1, 1, base - 1, 1)
					 .and(-4, base, -4, 4, base + height, 4)
					 .hasSpace(pos, isEmptySpace(world)))
			return false;
		
		int mainBranches = (treeType == TreeTypes.TYPE_1)? 2 + rand.nextInt(2) : 3 + rand.nextInt(6);
		
		for (int i = 0; i < mainBranches; ++i)
		{
			base = 2 + rand.nextInt(4);
			branchUp(world, pos, rand, height, (base >= height - 2)? height - 5 : base);
		}
		
		return true;
	}
	
	private void branchUp(World world, BlockPos pos, Random rand, int height, int base)
	{
		int fallX = 1 - rand.nextInt(3);
		int fallZ = 1 - rand.nextInt(3);
		int fallCount = 0;
		BlockPos upPos = pos.down();
		EnumAxis woodAxis;
		
		for (int i = 0; i < height; i++)
		{
			if (rand.nextInt(3) == 0 && i > base && fallCount < 3)
			{
				fallCount++;
				
				upPos = upPos.add(fallX, 0, fallZ);
				
				if (fallX != 0)
					woodAxis = EnumAxis.X;
				else if (fallZ != 0)
					woodAxis = EnumAxis.Z;
				else
					woodAxis = EnumAxis.Y;
				
				if (rand.nextInt(3) == 0 || (fallX == 0 && fallZ == 0))
					upPos = upPos.up();
			}
			else
			{
				fallCount = 0;
				
				woodAxis = EnumAxis.Y;
				upPos = upPos.up();
			}
			
			setBlockInWorld(world, upPos, wood.withProperty(BlockLog.LOG_AXIS, woodAxis));
			
			if (i == base)
				doBranchLeaves(world, upPos, rand, false, 2, true);
			
			if (i > base)
			{
				if (i > base + 2)
					doBranchLeaves(world, upPos.down(2), rand, false, 2, true);
				
				if (i > base + 1)
					doBranchLeaves(world, upPos.down(), rand, false, 3, true);
				
				doBranchLeaves(world, upPos, rand, true, 4, true);
			}
		}
	}
}
