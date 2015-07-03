package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class WorldGenTreeLepidodendron extends WorldGenTreeBase
{
	public WorldGenTreeLepidodendron(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.LEPIDODENDRON).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.LEPIDODENDRON),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		Block soil = world.getBlockState(pos.down()).getBlock();
		
		if (soil == null || !soil.canSustainPlant(world, pos, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.LEPIDODENDRON)))
		{
			return false;
		}
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight) - 4;
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight);
		int leaves = 1 + rand.nextInt(2);
		
		if (rand.nextInt(2) == 0)
		{
			doBranch(world, branchPos, 1, 0, rand, leaves, true);
			doBranch(world, branchPos, -1, 0, rand, leaves, true);
			doBranch(world, branchPos, 0, 1, rand, leaves, true);
			doBranch(world, branchPos, 0, -1, rand, leaves, true);
		}
		else
		{
			doBranch(world, branchPos, 1, 1, rand, leaves, true);
			doBranch(world, branchPos, -1, -1, rand, leaves, true);
			doBranch(world, branchPos, -1, 1, rand, leaves, true);
			doBranch(world, branchPos, 1, -1, rand, leaves, true);
		}
		
		doBranchLeaves(world, branchPos.down(), rand, false);
		
		return true;
	}
	
	private boolean doBranch(World world, BlockPos pos, int dirX, int dirZ, Random random, int leaveLength, boolean force)
	{
		boolean generated = false;
		
		if (random.nextInt(10) > 5 || force)
		{
			pos = pos.add((1 * dirX), 0, (1 * dirZ));
			
			setBlockInWorld(world, pos, wood);
			
			if (random.nextInt(10) > 1 || force)
			{
				doBranchLeaves(world, pos, random, false, leaveLength);
				
				pos = pos.add(0, 1, 0);
				
				setBlockInWorld(world, pos, wood);
				
				if (random.nextInt(10) > 4 || force)
				{
					doBranchLeaves(world, pos, random, false, leaveLength);
					
					pos = pos.add(0, 1, 0);
					
					setBlockInWorld(world, pos, wood);
					doBranchLeaves(world, pos, random, true, leaveLength);
				}
				else
				{
					doBranchLeaves(world, pos, random, true, leaveLength);
				}
			}
			else
			{
				doBranchLeaves(world, pos, random, true, leaveLength);
			}
			generated = true;
		}
		
		return generated;
	}
	
	private void doBranchLeaves(World world, BlockPos pos, Random random, boolean cap)
	{
		doBranchLeaves(world, pos, random, cap, 1 + random.nextInt(2));
	}
	
	private void doBranchLeaves(World world, BlockPos pos, Random random, boolean cap, int length)
	{
		for (int i = 1; i <= length; ++i)
		{
			setBlockInWorld(world, pos.north(i), leaves);
			setBlockInWorld(world, pos.north(i - 1).east(), leaves);
			setBlockInWorld(world, pos.north(i - 1).west(), leaves);
			
			setBlockInWorld(world, pos.south(i), leaves);
			setBlockInWorld(world, pos.south(i - 1).east(), leaves);
			setBlockInWorld(world, pos.south(i - 1).west(), leaves);
			
			setBlockInWorld(world, pos.east(i), leaves);
			setBlockInWorld(world, pos.east(i - 1).north(), leaves);
			setBlockInWorld(world, pos.east(i - 1).south(), leaves);
			
			setBlockInWorld(world, pos.west(i), leaves);
			setBlockInWorld(world, pos.west(i - 1).north(), leaves);
			setBlockInWorld(world, pos.west(i - 1).south(), leaves);
		}
		
		if (cap)
		{
			setBlockInWorld(world, pos.up(1), leaves);
			setBlockInWorld(world, pos.up(1).north(), leaves);
			setBlockInWorld(world, pos.up(1).south(), leaves);
			setBlockInWorld(world, pos.up(1).east(), leaves);
			setBlockInWorld(world, pos.up(1).west(), leaves);
			setBlockInWorld(world, pos.up(2), leaves);
		}
	}
}
