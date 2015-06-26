package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class WorldGenTreeLepidodendron extends WorldGenTreeBase
{
	public WorldGenTreeLepidodendron(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.LEPIDODENDRON),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.LEPIDODENDRON),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos)
	{
		this.world = worldIn;
		this.random = rand;
		
		int locY = pos.getY();
		
		for (
				@SuppressWarnings("unused")
				boolean check = false;
				(worldIn.getBlockState(pos).getBlock().isAir(worldIn, pos) || worldIn.getBlockState(pos).getBlock().isLeaves(worldIn, pos)) && locY > 0;
				--locY)
		{
			;
		}
		
		Block soil = worldIn.getBlockState(new BlockPos(pos.getX(), locY, pos.getZ())).getBlock();
		
		if (soil == null || !soil.canSustainPlant(worldIn, pos, EnumFacing.UP, (IPlantable)GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.LEPIDODENDRON)))
			return false;
		
		if (!isCubeClear(pos.getX(), locY + 2, pos.getZ(), 1, 15))
			return false;
		
		++locY;
		int treeHeight = minHeight + random.nextInt(maxHeight - minHeight) - 4;
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(new BlockPos(pos.getX(), locY + i, pos.getZ()), wood);
		}
		
		int curHeight = locY + treeHeight;
		int leaves = 1 + random.nextInt(2);
		
		if (random.nextInt(2) == 0)
		{
			doBranch(pos.getX(), curHeight, pos.getZ(), 1, 0, random, leaves, true);
			doBranch(pos.getX(), curHeight, pos.getZ(), -1, 0, random, leaves, true);
			doBranch(pos.getX(), curHeight, pos.getZ(), 0, 1, random, leaves, true);
			doBranch(pos.getX(), curHeight, pos.getZ(), 0, -1, random, leaves, true);
		}
		else
		{
			doBranch(pos.getX(), curHeight, pos.getZ(), 1, 1, random, leaves, true);
			doBranch(pos.getX(), curHeight, pos.getZ(), -1, -1, random, leaves, true);
			doBranch(pos.getX(), curHeight, pos.getZ(), -1, 1, random, leaves, true);
			doBranch(pos.getX(), curHeight, pos.getZ(), 1, -1, random, leaves, true);
		}
		
		doBranchLeaves(new BlockPos(pos.getX(), curHeight - 1, pos.getZ()), random, false);
		
		return true;
	}
	
	private boolean doBranch(int x, int y, int z, int dirX, int dirZ, Random random, int leaveLength, boolean force)
	{
		boolean generated = false;
		BlockPos curPos = new BlockPos(x, y, z);
		
		if (random.nextInt(10) > 5 || force)
		{
			curPos = curPos.add((1 * dirX), 0, (1 * dirZ));
			
			setBlockInWorld(curPos, wood);
			
			if (random.nextInt(10) > 1 || force)
			{
				doBranchLeaves(curPos, random, false, leaveLength);
				
				curPos = curPos.add(0, 1, 0);
				
				setBlockInWorld(curPos, wood);
				
				if (random.nextInt(10) > 4 || force)
				{
					doBranchLeaves(curPos, random, false, leaveLength);
					
					curPos = curPos.add(0, 1, 0);
					
					setBlockInWorld(curPos, wood);
					doBranchLeaves(curPos, random, true, leaveLength);
				}
				else
				{
					doBranchLeaves(curPos, random, true, leaveLength);
				}
			}
			else
			{
				doBranchLeaves(curPos, random, true, leaveLength);
			}
			generated = true;
		}
		
		return generated;
	}
	
	private void doBranchLeaves(BlockPos pos, Random random, boolean cap)
	{
		doBranchLeaves(pos, random, cap, 1 + random.nextInt(2));
	}
	
	private void doBranchLeaves(BlockPos pos, Random random, boolean cap, int length)
	{
		for (int i = 1; i <= length; ++i)
		{
			setBlockInWorld(pos.north(i), leaves);
			setBlockInWorld(pos.north(i - 1).east(), leaves);
			setBlockInWorld(pos.north(i - 1).west(), leaves);
			
			setBlockInWorld(pos.south(i), leaves);
			setBlockInWorld(pos.south(i - 1).east(), leaves);
			setBlockInWorld(pos.south(i - 1).west(), leaves);
			
			setBlockInWorld(pos.east(i), leaves);
			setBlockInWorld(pos.east(i - 1).north(), leaves);
			setBlockInWorld(pos.east(i - 1).south(), leaves);
			
			setBlockInWorld(pos.west(i), leaves);
			setBlockInWorld(pos.west(i - 1).north(), leaves);
			setBlockInWorld(pos.west(i - 1).south(), leaves);
		}
		
		if (cap)
		{
			setBlockInWorld(pos.up(1), leaves);
			setBlockInWorld(pos.up(1).north(), leaves);
			setBlockInWorld(pos.up(1).south(), leaves);
			setBlockInWorld(pos.up(1).east(), leaves);
			setBlockInWorld(pos.up(1).west(), leaves);
			setBlockInWorld(pos.up(2), leaves);
		}
	}
}
