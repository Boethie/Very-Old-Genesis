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
		super(GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.DEAD_LOG, treeType),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, treeType), notify);

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
					generateLogs(world, rand, pos, 1, 1);
					break;
				case 1:
					generateLogs(world, rand, pos, 2, 2);
					break;
				default:
					generateLogs(world, rand, pos, 1, 1);
					break;
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	private void generateLogs(World world, Random rand, BlockPos pos, int logWidth, int logHeight)
	{
		int length = minHeight + rand.nextInt(maxHeight);
		int lengthTop = rand.nextInt(maxHeight) / 2 + 1;
		int logOffset = rand.nextInt(3);
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
					logPos = logPos.add(-currentLogLength + rand.nextInt(minHeight), 1, 0);
					currentLogLength = lengthTop;
				}
				logPos = logPos.add(-currentLogLength + logOffset, -logHeight, 1);
				currentLogLength = length;
				
				// Random nth log
				length = minHeight + rand.nextInt(maxHeight);
				lengthTop = rand.nextInt(maxHeight) / 2 + 1;
				logOffset = rand.nextInt(3);
			}
		}
		else
		{
			logPos = pos.add(0, 0, (length / 2) * -1);
			for (int k = 0; k < logWidth; ++k)
			{
				for (int j = 0; j < logHeight; ++j)
				{
					for (int i = 0; i < currentLogLength; ++i)
					{
						setBlockInWorld(world, logPos, wood.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z));
						
						if (rand.nextInt(10) > 7 && topDecorations.size() > 0)
						{
							setBlockInWorld(world, logPos.up(), topDecorations.get(rand.nextInt(topDecorations.size())));
						}
						
						logPos = logPos.add(0, 0, 1);
						
					}
					logPos = logPos.add(0, 1, -currentLogLength + rand.nextInt(minHeight));
					currentLogLength = lengthTop;
				}
				logPos = logPos.add(1, -logHeight, -currentLogLength + logOffset);
				currentLogLength = length;
				
				// Random nth log
				length = minHeight + rand.nextInt(maxHeight);
				lengthTop = rand.nextInt(maxHeight) / 2 + 1;
				logOffset = rand.nextInt(minHeight) + 2;
			}
		}
	}
}
