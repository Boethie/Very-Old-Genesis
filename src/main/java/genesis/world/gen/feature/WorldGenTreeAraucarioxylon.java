package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

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
}
