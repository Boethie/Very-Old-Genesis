package genesis.world.gen.feature;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenTreeAraucarioxylon extends WorldGenTreeBase
{
	private boolean generateRandomSaplings = true;
	private int treeType = 0;
	
	public WorldGenTreeAraucarioxylon(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.ARAUCARIOXYLON).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.ARAUCARIOXYLON),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	public WorldGenTreeAraucarioxylon setType(int type)
	{
		treeType = type;
		return this;
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
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight - 1);
		
		int leavesBase = 0;
		boolean alternate = true;
		boolean irregular = false;
		boolean inverted = false;
		
		switch (treeType)
		{
		case 1:
			leavesBase = branchPos.getY() - 4 - rand.nextInt(6);
			alternate = false;
			inverted = true;
			irregular = false;
			break;
		default:
			leavesBase = pos.getY() + 6;
			break;
		}
		
		int base = 4 + rand.nextInt(4);
		int direction = 0;
		
		for (int i = base; i < treeHeight && treeType == 0; ++i)
		{
			++direction;
			if (direction > 7)
				direction = 0;
			
			branchDown(world, pos.up(i), rand, pos.getY(), direction + 1);
		}
		
		doPineTopLeaves(world, pos, branchPos, treeHeight, leavesBase, rand, alternate, irregular, inverted);
		
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
						&& world.getBlockState(posSapling).getBlock().canSustainPlant(world, posSapling, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.ARAUCARIOXYLON)))
				{
					setBlockInWorld(world, posSapling.up(), GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.SAPLING, EnumTree.ARAUCARIOXYLON));
				}
			}
		}
		
		return true;
	}
	
	private void branchDown(World world, BlockPos pos, Random rand, int groundLevel, int direction)
	{
		int fallX = 1;
		int fallZ = 1;
		int fallCount = 0;
		BlockPos upPos = pos.down();
		EnumAxis woodAxis = EnumAxis.Y;
		
		int fallDistance = 5 + rand.nextInt(2);
		
		switch(direction)
		{
		case 0:
			fallX = 0;
			fallZ = 0;
			break;
		case 1:
			fallX = 0;
			fallZ = 1;
			break;
		case 2:
			fallX = 1;
			fallZ = 1;
			break;
		case 3:
			fallX = 1;
			fallZ = 0;
			break;
		case 4:
			fallX = 1;
			fallZ = -1;
			break;
		case 5:
			fallX = 0;
			fallZ = -1;
			break;
		case 6:
			fallX = -1;
			fallZ = -1;
			break;
		case 7:
			fallX = -1;
			fallZ = 0;
		case 8:
			fallX = -1;
			fallZ = 1;
			break;
		}
		
		for (int i = 0; i < fallDistance; i++)
		{
			if (upPos.getY() < groundLevel + 3)
				return;
			
			if (rand.nextInt(3) == 0 || fallCount > 3)
			{
				fallCount = 0;
				
				upPos = upPos.add(fallX, 0, fallZ);
				
				if (fallX != 0)
					woodAxis = EnumAxis.X;
				else if (fallZ != 0)
					woodAxis = EnumAxis.Z;
				else
					woodAxis = EnumAxis.Y;
				
				if (rand.nextInt(3) == 0 || (fallX == 0 && fallZ == 0))
					upPos = upPos.down();
			}
			else
			{
				fallCount++;
				
				woodAxis = EnumAxis.Y;
				upPos = upPos.down();
			}
			
			setBlockInWorld(world, upPos, wood.withProperty(BlockLog.LOG_AXIS, woodAxis));
			
			doBranchLeaves(world, upPos, rand, true, 2, true);
			
			if (i == fallDistance - 1)
				doBranchLeaves(world, upPos.down(), rand, false, 1, true);
		}
	}
}
