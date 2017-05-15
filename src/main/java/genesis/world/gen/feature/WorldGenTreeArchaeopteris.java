package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.BlockVolumeShape;
import genesis.util.random.i.IntRange;
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
				setBlockInWorld(world, pos.up(i), wood);
				break;
			}
		}
		
		BlockPos branchPos = pos.up(height - 1);
		
		switch (treeType)
		{
		case TYPE_2:
			doPineTopLeaves(world, pos, branchPos.add(0, 0, 0), height, branchPos.down(branchY).getY(), rand, false);
			doPineTopLeaves(world, pos, branchPos.add(1, 0, 1), height, branchPos.down(branchY).getY(), rand, false);
			doPineTopLeaves(world, pos, branchPos.add(1, 0, 0), height, branchPos.down(branchY).getY(), rand, false);
			doPineTopLeaves(world, pos, branchPos.add(0, 0, 1), height, branchPos.down(branchY).getY(), rand, false);
			break;
		default:
			doPineTopLeaves(world, pos, branchPos, height, branchPos.down(branchY).getY(), rand, false);
			break;
		}
		
		//doPineTopLeaves(world, pos, branchPos, height, branchPos.down(branchY).getY(), rand, false);
		
		return true;
	}
}
