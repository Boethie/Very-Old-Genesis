package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.BlockVolumeShape;
import genesis.util.random.i.IntRange;
import net.minecraft.block.BlockLog;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeArchaeopteris extends WorldGenTreeBase
{
	public WorldGenTreeArchaeopteris(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.ARCHAEOPTERIS, IntRange.create(minHeight, maxHeight), notify);
	}
	
	@Override
	protected boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand) - 5;
		int branchY = 3 + rand.nextInt(4);
		
		if (!BlockVolumeShape.region(-1, 1, -1, 1, branchY - 1, 1)
					 .and(-2, branchY, -2, 2, height + 2, 2)
					 .hasSpace(pos, isEmptySpace(world)))
			return false;
		
		BlockPos checkPos = pos;
		
		if (treeType == TreeTypes.TYPE_2
				&& (getTreePos(world, checkPos = checkPos.east(), 1) == null
					|| getTreePos(world, checkPos = checkPos.south(), 1) == null)
					|| getTreePos(world, checkPos = checkPos.west(), 1) == null)
			return false;
		
		for (BlockPos cornerPos : BlockPos.getAllInBoxMutable(pos, pos.add(1, 0, 1)))
		{
			if (cornerPos.equals(pos))
				continue;
			
			BlockPos groundPos = getTreePos(world, cornerPos);
			
			if (groundPos == null)
				return false;
			
			pos = new BlockPos(pos.getX(), Math.min(pos.getY(), groundPos.getY()), pos.getZ());
		}
		
		int rootMaxHeight = 3;
		int[] roots = {rand.nextInt(rootMaxHeight), rand.nextInt(rootMaxHeight), rand.nextInt(rootMaxHeight), rand.nextInt(rootMaxHeight)};
		
		if ((roots[0] + roots[1] + roots[2] + roots[3]) / 4 == roots[0] * 4)
		{
			int rand_root = rand.nextInt(rootMaxHeight);
			int rand_height = rand.nextInt(30) % (rootMaxHeight - 1);
			roots[rand_root] = rand_height;
			
			if (rand_height - 1 > 0)
			{
				rand_root = (rand_root + 1) % (rootMaxHeight - 1);
				roots[rand_root] = rand_height - 1;
			}
		}
		
		for (int i = 0; i < height; i++)
		{
			switch (treeType)
			{
			case TYPE_2:
				setBlockInWorld(world, pos.add(1, i, 0), wood);
				setBlockInWorld(world, pos.add(0, i, 1), wood);
				setBlockInWorld(world, pos.add(1, i, 1), wood);
				setBlockInWorld(world, pos.add(0, i, 0), wood);
				break;
			default:
				if (rand.nextInt(2) == 0)
				{
					for (EnumFacing f : EnumFacing.HORIZONTALS)
					{
						if (i < roots[f.getHorizontalIndex()])
						{
							setBlockInWorld(world, pos.offset(f).up(i), wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
						}
					}
				}
				
				setBlockInWorld(world, pos.up(i), wood);
				break;
			}
		}
		
		BlockPos branchPos = pos.up(height - 1);
		
		doPineTopLeaves(world, pos, branchPos, height, branchPos.down(branchY).getY(), rand, false);
		
		return true;
	}
}
