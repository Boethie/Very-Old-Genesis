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

public class WorldGenTreeArchaeopteris extends WorldGenTreeBase
{
	public WorldGenTreeArchaeopteris(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.ARCHAEOPTERIS).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.ARCHAEOPTERIS),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		Block soil = world.getBlockState(pos.down()).getBlock();
		
		if (
				soil == null 
				|| !soil.canSustainPlant(world, pos, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.ARCHAEOPTERIS))
				|| !world.getBlockState(pos).getBlock().isAir(world, pos))
		{
			return false;
		}
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight) - 5;
		
		if (!isCubeClear(world, pos.up(), 1, treeHeight))
		{
			return false;
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
		}
		
		BlockPos branchPos = pos.up(treeHeight - 1);
		
		int branchY = 3 + rand.nextInt(3);
		
		doPineTopLeaves(world, pos, branchPos, treeHeight, branchPos.down(branchY).getY(), rand, false, 3);
		/*
		branchY += 2;
		
		if (rand.nextInt(3) == 0)
			doRandomBranch(world, branchPos.down(branchY + rand.nextInt(3)), rand, 1, 0);
		if (rand.nextInt(3) == 0)
			doRandomBranch(world, branchPos.down(branchY + rand.nextInt(3)), rand, -1, 0);
		if (rand.nextInt(3) == 0)
			doRandomBranch(world, branchPos.down(branchY + rand.nextInt(3)), rand, 0, 1);
		if (rand.nextInt(3) == 0)
			doRandomBranch(world, branchPos.down(branchY + rand.nextInt(3)), rand, 0, -1);
		*/
		return true;
	}
	/*
	private void doRandomBranch(World world, BlockPos pos, Random rand, int dirX, int dirZ)
	{
		BlockPos branchPos = pos;
		int height = 2 + rand.nextInt(2);
		
		branchPos = branchPos.add(1 * dirX, 0, 1 * dirZ);
		setBlockInWorld(world, branchPos, wood);
		
		branchPos = branchPos.add(1 * dirX, rand.nextInt(2), 1 * dirZ);
		setBlockInWorld(world, branchPos, wood);
		
		branchPos = branchPos.add(1 * dirX, 1, 1 * dirZ);
		
		for (int i = 0; i < height; ++i)
		{
			setBlockInWorld(world, branchPos, wood);
			branchPos = branchPos.up();
		}
		
		doPineTopLeaves(world, pos, branchPos.down(), height, branchPos.getY() - height + 1, rand, false, 3);
	}
	*/
}
