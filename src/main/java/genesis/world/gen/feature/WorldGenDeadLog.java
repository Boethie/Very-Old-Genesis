package genesis.world.gen.feature;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;

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
	private int treeType = 0;
	
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
	
	public WorldGenDeadLog setType(int type)
	{
		treeType = type;
		return this;
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
			
			switch (treeType)
			{
			case 0:
				generateSingleLogs(world, rand, pos);
			case 1:
				generateMultiLogs(world, rand, pos, 2, 2);
				break;

			default:
				generateSingleLogs(world, rand, pos);
				break;
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
		
	private void generateMultiLogs(World world, Random rand, BlockPos pos, int logWidth, int logHeight)
	{
		int length = minHeight + rand.nextInt(maxHeight);
		int lengthTop = rand.nextInt(maxHeight) / 2 + 1;
		int logOffset = rand.nextInt(minHeight) + 2;
		int currentLogLength = length;
		
		BlockPos logPos;
		
		if (rand.nextInt(2) == 0)
		{
			logPos = pos.add((length / 2) * -1, 0, 0);
			for (int k = 0; k < logWidth; ++k)
			{
				for (int j = 0; j < logHeight; ++j)
				{
					for (int i = 0; i < currentLogLength; ++i)
					{
						setBlockInWorld(world, logPos, wood.withProperty(BlockLog.LOG_AXIS, EnumAxis.X));
						
						if (rand.nextInt(100) > 96 && topDecorations.size() > 0)
						{
							setBlockInWorld(world, logPos.up(),
									topDecorations.get(rand.nextInt(topDecorations.size())));
						}
						
						logPos = logPos.add(1, 0, 0);
						
					}
					logPos = logPos.add(-currentLogLength, 1, 0);
					currentLogLength = lengthTop;
				}
				logPos = logPos.add(-currentLogLength + 5, -2, 1);
				currentLogLength = length;
				
				// Random 2nd log
				length = minHeight + rand.nextInt(maxHeight);
				lengthTop = rand.nextInt(maxHeight) / 2 + 1;
			}
		}
		else
		{
			// Still need to do Z
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
	}
	
	private void generateSingleLogs(World world, Random rand, BlockPos pos)
	{
		int currentLogLength = minHeight + rand.nextInt(maxHeight);
		BlockPos logPos;
		if (rand.nextInt(2) == 0)
		{
			logPos = pos.add((currentLogLength / 2) * -1, 0, 0);
			for (int i = 0; i < currentLogLength; ++i)
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
			logPos = pos.add(0, 0, (currentLogLength / 2) * -1);
			
			for (int i = 0; i < currentLogLength; ++i)
			{
				setBlockInWorld(world, logPos, wood.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z));
				
				if (rand.nextInt(10) > 7 && topDecorations.size() > 0)
				{
					setBlockInWorld(world, logPos.up(), topDecorations.get(rand.nextInt(topDecorations.size())));
				}
				
				logPos = logPos.add(0, 0, 1);
			}
		}
	}	
}
