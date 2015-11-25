package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.metadata.TreeBlocksAndItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenDeadLog extends WorldGenTreeBase
{
	private List<IBlockState> topDecorations = new ArrayList<IBlockState>();
	
	public WorldGenDeadLog(int minLength, int maxLength, EnumTree treeType, boolean notify)
	{
		super(
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.DEAD_LOG, treeType),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, treeType),
				notify);
		
		this.notify = notify;
		
		this.minHeight = minLength;
		this.maxHeight = maxLength;
	}
	
	public WorldGenTreeBase addTopDecoration(IBlockState block)
	{
		topDecorations.add(block);
		return this;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		try
		{
			pos = getTreePos(world, pos);
			
			if (!canTreeGrow(world, pos))
				return false;
			
			int length = minHeight + rand.nextInt(maxHeight);
			
			if (!isCubeClear(world, pos.up(), length, 1))
			{
				return false;
			}
			
			BlockPos logPos;
			
			if (rand.nextInt(2) == 0)
			{
				logPos = pos.add((length / 2) * -1, 0, 0);
				
				for (int i = 0; i < length; ++i)
				{
					setBlockInWorld(world, logPos, wood.withProperty(BlockLog.LOG_AXIS, EnumAxis.X));
					
					if (rand.nextInt(100) > 96 && topDecorations.size() > 0)
					{
						setBlockInWorld(world, logPos.up(), topDecorations.get(rand.nextInt(topDecorations.size())));
					}
					
					logPos = logPos.add(1, 0, 0);
				}
			}
			else
			{
				logPos = pos.add(0, 0, (length / 2) * -1);
				
				for (int i = 0; i < length; ++i)
				{
					setBlockInWorld(world, logPos, wood.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z));
					
					if (rand.nextInt(10) > 7 && topDecorations.size() > 0)
					{
						setBlockInWorld(world, logPos.up(), topDecorations.get(rand.nextInt(topDecorations.size())));
					}
					
					logPos = logPos.add(0, 0, 1);
				}
			}
			
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
