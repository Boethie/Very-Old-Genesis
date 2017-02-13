package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.BlockVolumeShape;
import genesis.util.random.i.IntRange;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeFicus extends WorldGenTreeBase
{
	public WorldGenTreeFicus(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.FICUS, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		int base = 3 + rand.nextInt(3);
		
		if (!BlockVolumeShape.region(-1, 1, -1, 1, base - 1, 1)
					 .and(-5, base, -5, 5, base + height, 5)
					 .hasSpace(pos, isEmptySpace(world)))
			return false;
		
		int mainBranches = 1 + rand.nextInt(8);
		
		for (int i = 0; i < mainBranches; ++i)
			branchUp(world, pos, rand, height, base);
		
		return true;
	}
	
	private void branchUp(World world, BlockPos pos, Random rand, int height, int base)
	{
		int fallX = 1 - rand.nextInt(3);
		int fallZ = 1 - rand.nextInt(3);
		
		BlockPos upPos = pos.down();
		EnumAxis woodAxis = EnumAxis.Y;
		
		for (int i = 0; i < height; i++)
		{
			if (i > base)
			{
				upPos = upPos.add(fallX, 0, fallZ);
				
				if (fallX != 0)
					woodAxis = EnumAxis.X;
				else if (fallZ != 0)
					woodAxis = EnumAxis.Z;
				else
					woodAxis = EnumAxis.Y;
			}
			
			upPos = upPos.up();
			
			setBlockInWorld(world, upPos, wood.withProperty(BlockLog.LOG_AXIS, woodAxis));
			
			if (i > base - (2 + rand.nextInt(2)))
				doBranchLeaves(world, upPos, rand, (i >= height - 1), 3 + rand.nextInt(2), true);
		}
	}
}
