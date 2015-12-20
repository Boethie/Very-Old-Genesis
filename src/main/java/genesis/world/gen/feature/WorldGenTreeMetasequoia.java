package genesis.world.gen.feature;

import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenTreeMetasequoia extends WorldGenTreeBase
{
	private boolean generateRandomSaplings = true;
	private int treeType = 0;
	private int offsetX = 0;
	private int offsetZ = 0;
	
	public WorldGenTreeMetasequoia(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.METASEQUOIA).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.METASEQUOIA),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	public WorldGenTreeMetasequoia setType(int type, int oX, int oZ)
	{
		treeType = type;
		offsetX = oX;
		offsetZ = oZ;
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
		
		if (
				(treeType == 1
						&& (!canTreeGrow(world, pos.add(0 + offsetX, 0, 0 + offsetZ))
								|| !canTreeGrow(world, pos.add(1 + offsetX, 0, 1 + offsetZ))
								|| !canTreeGrow(world, pos.add(1 + offsetX, 0, 0 + offsetZ))
								|| !canTreeGrow(world, pos.add(0 + offsetX, 0, 1 + offsetZ))))
				|| (treeType == 0 && !canTreeGrow(world, pos)))
			return false;
		
		if (rand.nextInt(rarity) != 0)
			return false;
		
		int treeHeight = minHeight + rand.nextInt(maxHeight - minHeight);
		
		if (!isCubeClear(world, pos.up(), (treeType == 0)? 2 : 3, treeHeight))
		{
			return false;
		}
		
		if (treeType == 1)
		{
			setBlockInWorld(world, pos.add(1 + offsetX, 0, 0 + offsetZ), wood, true);
			setBlockInWorld(world, pos.add(0 + offsetX, 0, 1 + offsetZ), wood, true);
			setBlockInWorld(world, pos.add(1 + offsetX, 0, 1 + offsetZ), wood, true);
			setBlockInWorld(world, pos.add(0 + offsetX, 0, 0 + offsetZ), wood, true);
		}
		
		for (int i = 0; i < treeHeight; i++)
		{
			if (treeType == 1)
			{
				setBlockInWorld(world, pos.up(i).add(1 + offsetX, 0, 0 + offsetZ), wood);
				setBlockInWorld(world, pos.up(i).add(0 + offsetX, 0, 1 + offsetZ), wood);
				setBlockInWorld(world, pos.up(i).add(1 + offsetX, 0, 1 + offsetZ), wood);
				setBlockInWorld(world, pos.up(i).add(0 + offsetX, 0, 0 + offsetZ), wood);
			}
			else
			{
				setBlockInWorld(world, pos.up(i), wood);
			}
		}
		
		BlockPos branchPos = pos.up(treeHeight - 1);
		
		int leavesBase = 0;
		boolean alternate = false;
		boolean irregular = true;
		boolean inverted = false;
		
		leavesBase = pos.getY() + 6;
		
		switch (treeType)
		{
		case 1:
			doPineTopLeaves(world, pos, branchPos.add(0 + offsetX, 0, 0 + offsetZ), treeHeight, leavesBase, rand, alternate, irregular, inverted);
			doPineTopLeaves(world, pos, branchPos.add(1 + offsetX, 0, 1 + offsetZ), treeHeight, leavesBase, rand, alternate, irregular, inverted);
			doPineTopLeaves(world, pos, branchPos.add(1 + offsetX, 0, 0 + offsetZ), treeHeight, leavesBase, rand, alternate, irregular, inverted);
			doPineTopLeaves(world, pos, branchPos.add(0 + offsetX, 0, 1 + offsetZ), treeHeight, leavesBase, rand, alternate, irregular, inverted);
			
			break;
		default:
			doPineTopLeaves(world, pos, branchPos, treeHeight, leavesBase, rand, alternate, irregular, inverted);
			break;
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
						&& world.getBlockState(posSapling).getBlock().canSustainPlant(world, posSapling, EnumFacing.UP, GenesisBlocks.trees.getBlock(TreeBlocksAndItems.SAPLING, EnumTree.METASEQUOIA)))
				{
					setBlockInWorld(world, posSapling.up(), GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.SAPLING, EnumTree.METASEQUOIA));
				}
			}
		}
		
		return true;
	}
}
