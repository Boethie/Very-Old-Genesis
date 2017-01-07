package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.variant.EnumTree;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.WeightedIntItem;
import genesis.util.random.i.WeightedIntProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeMetasequoia extends WorldGenTreeBase
{
	public WorldGenTreeMetasequoia(int minHeight, int maxHeight, boolean notify)
	{
		super(EnumTree.METASEQUOIA, IntRange.create(minHeight, maxHeight), notify);
		
		this.saplingCountProvider = new WeightedIntProvider(
				WeightedIntItem.of(104, 0),
				WeightedIntItem.of(6, IntRange.create(1, 3)));
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		int trunkHeight = 3 + rand.nextInt(4);
		int leavesBase = pos.getY() + trunkHeight;
		
		if (!isCubeClear(world, pos, treeType == TreeTypes.TYPE_1 ? 0 : 1, trunkHeight))
			return false;
		
		if (!isCubeClear(world, pos.up(trunkHeight + 1), treeType == TreeTypes.TYPE_1 ? 2 : 3, height - trunkHeight))
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
			doPineTopLeaves(world, pos, branchPos.add(0, 0, 0), height, leavesBase, rand, false, true, false);
			doPineTopLeaves(world, pos, branchPos.add(1, 0, 1), height, leavesBase, rand, false, true, false);
			doPineTopLeaves(world, pos, branchPos.add(1, 0, 0), height, leavesBase, rand, false, true, false);
			doPineTopLeaves(world, pos, branchPos.add(0, 0, 1), height, leavesBase, rand, false, true, false);
			break;
		default:
			doPineTopLeaves(world, pos, branchPos, height, leavesBase, rand, false, true, false);
			break;
		}
		
		switch (treeType)
		{
		case TYPE_2:
			generateResin(world, pos.add(1, 0, 0), height);
			generateResin(world, pos.add(0, 0, 1), height);
			generateResin(world, pos.add(1, 0, 1), height);
			generateResin(world, pos.add(0, 0, 0), height);
			break;
		default:
			generateResin(world, pos, height);
			break;
		}
		
		return true;
	}
}
