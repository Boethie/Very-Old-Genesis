package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenTreeDryophyllum extends WorldGenTreeBase
{
	private boolean generateRandomSaplings = true;
	
	public WorldGenTreeDryophyllum(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.DRYOPHYLLUM).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.DRYOPHYLLUM),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	public WorldGenTreeBase setGenerateRandomSaplings(boolean generate)
	{
		generateRandomSaplings = generate;
		return this;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		pos = getTreePos(world, pos);
		
		if (!canTreeGrow(world, pos))
			return false;
		
		if (rand.nextInt(rarity) != 0)
			return false;
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight);
		int base = 4 + rand.nextInt(4);
		
		if (!isCubeClear(world, pos.up(base), 3, treeHeight))
		{
			return false;
		}
		
		int mainBranches = 4 + rand.nextInt(8);
		
		for (int i = 0; i < mainBranches; ++i)
		{
			base = 4 + rand.nextInt(10);
			branchUp(world, pos, rand, treeHeight, base);
		}
		
		if (generateRandomSaplings && rand.nextInt(10) > 3)
		{
			int saplingCount = rand.nextInt(5);
			BlockPos posSapling;
			for (int si = 1; si <= saplingCount; ++si)
			{
				posSapling = pos.add(rand.nextInt(9) - 4, 0, rand.nextInt(9) - 4);
				
				if (
						posSapling != null
						&& world.getBlockState(posSapling.up()).getBlock().isAir(world, posSapling)
						&& world.getBlockState(posSapling).getBlock().canSustainPlant(world, posSapling, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.DRYOPHYLLUM)))
				{
					setBlockInWorld(world, posSapling.up(), GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.SAPLING, EnumTree.DRYOPHYLLUM));
				}
			}
		}
		
		return true;
	}
	
	private void branchUp(World world, BlockPos pos, Random rand, int height, int base)
	{
		int fallX = 1 - rand.nextInt(3);
		int fallZ = 1 - rand.nextInt(3);
		int fallCount = 0;
		BlockPos upPos = pos.down();
		EnumAxis woodAxis = EnumAxis.Y;
		
		for (int i = 0; i < height; i++)
		{
			if (rand.nextInt(3) == 0 && i > base && fallCount < 2)
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
			
			if (i == base - 1)
				doBranchLeaves(world, upPos, rand, false, 2, true);
			
			if (i > base - 1)
			{
				doBranchLeaves(world, upPos, rand, (i >= height - 3), 4, true);
			}
		}
	}
}
