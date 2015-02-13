package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBoulder extends WorldGenerator 
{
	private Block block;
	private int field_150544_b;

	public WorldGenBoulder(Block block, int size) 
	{
		super(false);
		this.block = block;
		this.field_150544_b = size;
	}

	public boolean generate(World world, Random rand, BlockPos pos) 
	{
		while (true) 
		{
			if (pos.getY() > 3) 
			{
				label63:
				{
				if (!world.isAirBlock(pos)) 
				{
					IBlockState blockState = world.getBlockState(pos.add(0, -1, 0));

					if (blockState.getBlock() == Blocks.grass || blockState.getBlock() == Blocks.dirt || blockState.getBlock() == Blocks.stone || blockState.getBlock() == GenesisBlocks.moss) 
					{
						break label63;
					}
				}

				pos = pos.add(0, -1, 0);
				continue;
				}
			}

			if (pos.getY() <= 3) 
			{
				return false;
			}

			int k2 = this.field_150544_b;

			for (int l = 0; k2 >= 0 && l < 3; ++l) 
			{
				int i1 = k2 + rand.nextInt(2);
				int j1 = k2 + rand.nextInt(2);
				int k1 = k2 + rand.nextInt(2);
				float f = (float) (i1 + j1 + k1) * 0.333F + 0.5F;

				for (int l1 = pos.getX() - i1; l1 <= pos.getX() + i1; ++l1) 
				{
					for (int i2 = pos.getZ() - k1; i2 <= pos.getZ() + k1; ++i2) 
					{
						for (int j2 = pos.getY() - j1; j2 <= pos.getY() + j1; ++j2) 
						{
							float f1 = (float) (l1 - pos.getX());
							float f2 = (float) (i2 - pos.getY());
							float f3 = (float) (j2 - pos.getZ());

							if (f1 * f1 + f2 * f2 + f3 * f3 <= f * f)
							{
								world.setBlockState(new BlockPos(l1, j2, i2), this.block.getDefaultState(), 4);
							}
						}
					}
				}

				pos = pos.add(-(k2 + 1) + rand.nextInt(2 + k2 * 2), 0 - rand.nextInt(2), -(k2 + 1) + rand.nextInt(2 + k2 * 2));
			}

			return true;
		}
	}
}