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
				WeightedIntItem.of(4, 0),
				WeightedIntItem.of(6, IntRange.create(2, 5)));
	}
	
	@Override
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int height = heightProvider.get(rand);
		
		int trunkHeight = 6;
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
			case TYPE_1:
				setBlockInWorld(world, pos.up(i), wood);
				break;
			case TYPE_2:
				setBlockInWorld(world, pos.add(1, i, 0), wood);
				setBlockInWorld(world, pos.add(0, i, 1), wood);
				setBlockInWorld(world, pos.add(1, i, 1), wood);
				setBlockInWorld(world, pos.add(0, i, 0), wood);
				break;
			}
		}
		
		BlockPos branchPos = pos.up(height - 1);
		
		boolean alternate = false;
		boolean irregular = true;
		boolean inverted = false;
		
		switch (treeType)
		{
		case TYPE_1:
			doPineTopLeaves(world, pos, branchPos, height, leavesBase, rand, alternate, irregular, inverted);
			break;
		case TYPE_2:
			doPineTopLeaves(world, pos, branchPos.add(0, 0, 0), height, leavesBase, rand, alternate, irregular, inverted);
			doPineTopLeaves(world, pos, branchPos.add(1, 0, 1), height, leavesBase, rand, alternate, irregular, inverted);
			doPineTopLeaves(world, pos, branchPos.add(1, 0, 0), height, leavesBase, rand, alternate, irregular, inverted);
			doPineTopLeaves(world, pos, branchPos.add(0, 0, 1), height, leavesBase, rand, alternate, irregular, inverted);
			break;
		}
		/*
		if (generateRandomSaplings && rand.nextInt(10) > 3)
		{
			int saplingCount = rand.nextInt(5);
			BlockPos posSapling;
			
			for (int i = 1; i <= saplingCount; ++i)
			{
				posSapling = pos.add(rand.nextInt(9) - 4, 0, rand.nextInt(9) - 4);
				
				IBlockState checkState = world.getBlockState(posSapling);
				
				if (!checkState.getBlock().canSustainPlant(checkState, world, posSapling, EnumFacing.UP,
							GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.METASEQUOIA)))
					continue;
				
				checkState = world.getBlockState(posSapling.up());
				
				if (!checkState.getBlock().isAir(checkState, world, posSapling.up()))
					continue;
				
				setBlockInWorld(world, posSapling.up(), GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.SAPLING, EnumTree.METASEQUOIA));
			}
		}
		*/
		return true;
	}
}
