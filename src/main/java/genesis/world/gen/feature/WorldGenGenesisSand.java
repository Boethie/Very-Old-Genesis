package genesis.world.gen.feature;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenGenesisSand extends WorldGenerator
{
	private final Block sand;
	private final int radius;

	public WorldGenGenesisSand(Block sand, int radius)
	{
		this.sand = sand;
		this.radius = radius;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos position)
	{
		if (world.getBlockState(position).getMaterial() != Material.water)
		{
			return false;
		}
		
		int randRadius = rand.nextInt(radius - 2) + 2;
		byte yRadius = 2;

		for (int x = position.getX() - randRadius; x <= position.getX() + randRadius; ++x)
		{
			for (int z = position.getZ() - randRadius; z <= position.getZ() + randRadius; ++z)
			{
				int i = x - position.getX();
				int k = z - position.getZ();

				if (i * i + k * k <= randRadius * randRadius)
				{
					for (int y = position.getY() - yRadius; y <= position.getY() + yRadius; ++y)
					{
						BlockPos pos = new BlockPos(x, y, z);
						Block block = world.getBlockState(pos).getBlock();

						if (block == Blocks.dirt || block == GenesisBlocks.moss)
						{
							world.setBlockState(pos, sand.getDefaultState(), 2);
						}
					}
				}
			}
		}

		return true;
	}
}
