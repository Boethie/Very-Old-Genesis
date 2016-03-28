package genesis.world.gen.feature;

import java.util.Random;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTreeArchaeanthus extends WorldGenTreeBase
{
	public WorldGenTreeArchaeanthus(int minHeight, int maxHeight, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LOG, EnumTree.ARCHAEANTHUS).withProperty(BlockLog.LOG_AXIS, EnumAxis.Y),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, EnumTree.ARCHAEANTHUS),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
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
		int base = 2 + rand.nextInt(3);
		
		if (!isCubeClear(world, pos.up(base), 3, treeHeight))
		{
			return false;
		}
		
		//this.generateLeafLayerCircle(world, rand, 4, pos.up(4).getX(), pos.up(4).getZ(), pos.up(4).getY());
		
		int upCount = 0;
		int step = rand.nextInt(8);
		
		for (int i = 0; i < treeHeight; i++)
		{
			setBlockInWorld(world, pos.up(i), wood);
			
			upCount++;
			
			if (i > base && (rand.nextInt(2) == 0 || upCount > 1))
			{
				int fallX = 0;
				int fallZ = 0;
				float hProp = (1.0f - ((float)i / (float)treeHeight));
				int branchLength = (int)(5.0f * hProp);
				BlockPos branchPos = pos.up(i);
				
				EnumAxis woodAxis = EnumAxis.Y;
				
				switch (step)
				{
				case 1:
					fallX = -1;
					fallZ = 1;
					break;
				case 2:
					fallX = 1;
					fallZ = -1;
					break;
				case 3:
					fallX = -1;
					fallZ = -1;
					break;
				case 4:
					fallX = 0;
					fallZ = 1;
					break;
				case 5:
					fallX = 1;
					fallZ = 0;
					break;
				case 6:
					fallX = 0;
					fallZ = -1;
					break;
				case 7:
					fallX = -1;
					fallZ = 0;
					break;
				default:
					fallX = 1;
					fallZ = 1;
					break;
				}
				
				upCount = 0;
				step++;
				
				if (step > 7)
					step = 0;
				
				if (fallX != 0)
					woodAxis = EnumAxis.X;
				else if (fallZ != 0)
					woodAxis = EnumAxis.Z;
				else
					woodAxis = EnumAxis.Y;
				
				for (int j = 1; j <= branchLength; ++j)
				{
					if (rand.nextInt(2) == 0)
						branchPos = branchPos.up();
					
					setBlockInWorld(world, branchPos.add(j * fallX, 0, j * fallZ), wood.withProperty(BlockLog.LOG_AXIS, woodAxis));
					
					doBranchLeaves(world, branchPos.add(j * fallX, 0, j * fallZ).up(), rand, true, 2, true);
					doBranchLeaves(world, branchPos.add(j * fallX, 0, j * fallZ), rand, false, (int)Math.ceil(4.0f * hProp), true);
					doBranchLeaves(world, branchPos.add(j * fallX, 0, j * fallZ).down(), rand, false, 2, true);
				}
			}
			
			this.doPineTopLeaves(world, pos, pos.up(treeHeight - 1), treeHeight, pos.up(treeHeight).getY() - 5, rand, true, true);
		}
		
		return true;
	}
}
