package genesis.world.biome.decorate;

import genesis.block.BlockFern;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumFern;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenZygopteris extends WorldGenerator
{

	private final IBlockState state = GenesisBlocks.ferns.getBlockState(GenesisBlocks.ferns.soleType, EnumFern.ZYGOPTERIS);

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;
		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isAir(world, pos) && !block.isLeaves(world, pos))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);

		for (int i = 0; i < 128; ++i)
		{
			BlockPos placePos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8)
					- random.nextInt(8));

			if (world.isAirBlock(placePos) && ((BlockFern) this.state.getBlock()).canBlockStay(world, placePos, world.getBlockState(placePos)))
			{
				world.setBlockState(placePos, this.state, 2);
			}
		}

		return true;
	}

}
