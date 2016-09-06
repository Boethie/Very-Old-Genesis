package genesis.world.gen.feature;

import genesis.combo.TreeBlocksAndItems;
import genesis.combo.variant.EnumTree;
import genesis.common.GenesisBlocks;
import genesis.util.random.i.IntRange;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenDeadLog extends WorldGenTreeBase
{
	private List<IBlockState> topDecorations = new ArrayList<>();
	private int treeType = 0;

	private int minHeight;
	private int maxHeight;

	public WorldGenDeadLog(int minLength, int maxLength, EnumTree treeType, boolean notify)
	{
		super(GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.SAPLING, treeType),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.DEAD_LOG, treeType),
				GenesisBlocks.trees.getBlockState(TreeBlocksAndItems.LEAVES, treeType),
				null,
				IntRange.create(minLength, maxLength), notify);

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
	public boolean doGenerate(World world, Random rand, BlockPos pos)
	{
		int dirX;
		int dirZ;

		if (rand.nextInt(2) == 0)
		{
			dirX = 1;
			dirZ = 0;
		}
		else
		{
			dirX = 0;
			dirZ = 1;
		}

		int length = minHeight + (rand.nextInt(maxHeight - minHeight) + 1);

		for (BlockPos checkPos : BlockPos.getAllInBox(pos.add(((length / 2) * -1) * dirX, 0, ((length / 2) * -1) * dirZ), pos.add(((length / 2)) * dirX, treeType, ((length / 2)) * dirZ)))
		{
			IBlockState checkState = world.getBlockState(checkPos);

			if (
					!checkState.getBlock().isReplaceable(world, checkPos)
					&& !checkState.getBlock().isAir(checkState, world, checkPos)
					&& !(checkState.getMaterial() == Material.WATER && this.getCanGrowInWater()))
			{
				return false;
			}
		}

		switch (treeType)
		{
			case 1:
				generateLogs(world, rand, pos, length, 2, 2, dirX, dirZ);
				break;
			default:
				generateLogs(world, rand, pos, length, 1, 1, dirX, dirZ);
				break;
		}

		return true;
	}

	private void generateLogs(World world, Random rand, BlockPos pos, int length, int logWidth, int logHeight, int dirX, int dirZ)
	{
		int lengthTop = rand.nextInt(maxHeight) / 2 + 1;
		int logOffset = rand.nextInt(3);
		int currentLogLength = length;

		BlockPos logPos;

		int lDir = ((length / 2) * -1);
		logPos = pos.add(lDir * dirX, 0, lDir * dirZ);

		for (int k = 0; k < logWidth; ++k)
		{
			for (int j = 0; j < logHeight; ++j)
			{
				for (int i = 0; i < currentLogLength; ++i)
				{
					setBlockInWorld(world, logPos, wood.withProperty(BlockLog.LOG_AXIS, ((dirX == 1)? EnumAxis.X : EnumAxis.Z)));

					if (rand.nextInt(100) > 96 && topDecorations.size() > 0)
					{
						setBlockInWorld(world, logPos.up(),
								topDecorations.get(rand.nextInt(topDecorations.size())));
					}

					logPos = logPos.add(1 * dirX, 0, 1 * dirZ);
				}
				lDir = -currentLogLength + rand.nextInt(minHeight);

				logPos = logPos.add(lDir * dirX, 1, lDir * dirZ);
				currentLogLength = lengthTop;
			}
			lDir = -currentLogLength + logOffset;

			logPos = logPos.add((lDir * dirX) + (1 - dirX), -logHeight, (lDir * dirZ) + (1 - dirZ));
			currentLogLength = length;

			length = minHeight + (rand.nextInt(maxHeight - minHeight) + 1);
			lengthTop = rand.nextInt(maxHeight) / 2 + 1;
			logOffset = rand.nextInt(minHeight) + 2;
		}
	}
}
