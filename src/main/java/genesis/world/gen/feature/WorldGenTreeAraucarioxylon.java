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

public class WorldGenTreeAraucarioxylon extends WorldGenTreeBase
{
	private boolean generateRandomSaplings = true;
	
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
	
	public WorldGenTreeBase setGenerateRandomSaplings(boolean generate)
	{
		generateRandomSaplings = generate;
		return this;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		Block soil = world.getBlockState(pos.down()).getBlock();
		
		if (
				soil == null 
				|| !soil.canSustainPlant(world, pos, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.LEPIDODENDRON))
				|| !world.getBlockState(pos).getBlock().isAir(world, pos))
		{
			return false;
		}
		
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
		
		doPineTopLeaves(world, pos, branchPos, treeHeight, pos.getY() + 6, rand, true);
		
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
}
