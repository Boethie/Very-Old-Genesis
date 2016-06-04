package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.random.i.IntRange;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeArchaeanthus extends WorldGenTreeBase
{
	public WorldGenTreeArchaeanthus(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.ARCHAEANTHUS, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		int base = 2 + rand.nextInt(3);
		
		if (!isCubeClear(world, pos.up(base), 3, height))
		{
			return false;
		}
		
		int upCount = 0;
		int step = rand.nextInt(8);
		
		for (int i = 0; i < height; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
			
			upCount++;
			
			if (i > base && (rand.nextInt(2) == 0 || upCount > 1))
			{
				int fallX;
				int fallZ;
				float hProp = (1 - (i / (float) height));
				int branchLength = (int) (5 * hProp);
				BlockPos branchPos = pos.up(i);
				
				EnumAxis woodAxis;
				
				switch (step)
				{
				case 1:
					fallX = -1;
					fallZ = 1;
					break;
				case 2:
					fallX = 1;
					fallZ = -1;
					break;
				case 3:
					fallX = -1;
					fallZ = -1;
					break;
				case 4:
					fallX = 0;
					fallZ = 1;
					break;
				case 5:
					fallX = 1;
					fallZ = 0;
					break;
				case 6:
					fallX = 0;
					fallZ = -1;
					break;
				case 7:
					fallX = -1;
					fallZ = 0;
					break;
				default:
					fallX = 1;
					fallZ = 1;
					break;
				}
				
				upCount = 0;
				step++;
				
				if (step > 7)
					step = 0;
				
				if (fallX != 0)
					woodAxis = EnumAxis.X;
				else if (fallZ != 0)
					woodAxis = EnumAxis.Z;
				else
					woodAxis = EnumAxis.Y;
				
				for (int j = 1; j <= branchLength; ++j)
				{
					if (rand.nextInt(2) == 0)
						branchPos = branchPos.up();
					
					setBlockInWorld(world, branchPos.add(j * fallX, 0, j * fallZ), wood.withProperty(BlockLog.LOG_AXIS, woodAxis));
					
					doBranchLeaves(world, branchPos.add(j * fallX, 0, j * fallZ).up(), rand, true, 2, true);
					doBranchLeaves(world, branchPos.add(j * fallX, 0, j * fallZ), rand, false, (int) Math.ceil(4 * hProp), true);
					doBranchLeaves(world, branchPos.add(j * fallX, 0, j * fallZ).down(), rand, false, 2, true);
				}
			}
			
			doPineTopLeaves(world, pos, pos.up(height - 1), height, pos.up(height).getY() - 5, rand, true, true);
		}
		
		return true;
	}
}
