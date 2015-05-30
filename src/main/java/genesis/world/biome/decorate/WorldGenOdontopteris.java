package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenOdontopteris extends WorldGenerator
{

	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		Block block;

		do
		{
			block = world.getBlockState(pos).getBlock();
			if (!block.isLeaves(world, pos) && !block.isLeaves(world, pos))
			{
				break;
			}
			pos = pos.down();
		}
		while (pos.getY() > 0);

		for (int i = 0; i < 4; ++i)
		{
			BlockPos placePos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8)
					- random.nextInt(8));
			int growth = random.nextInt(7);
			IBlockState bottom = GenesisBlocks.odontopteris.getDefaultState().withProperty(GenesisBlocks.odontopteris.ageProp, growth).withProperty(GenesisBlocks.odontopteris.topProp, false);
			IBlockState top = GenesisBlocks.odontopteris.getDefaultState().withProperty(GenesisBlocks.odontopteris.ageProp, growth).withProperty(GenesisBlocks.odontopteris.topProp, true);
			Block soil = world.getBlockState(placePos.down()).getBlock();
			if (world.isAirBlock(placePos) && world.isAirBlock(placePos.up()) && (soil == Blocks.dirt || soil == GenesisBlocks.moss))
			{
				world.setBlockState(placePos, bottom, 2);
				if (GenesisBlocks.odontopteris.growthAge <= growth)
				{
					world.setBlockState(placePos.up(), top, 2);
				}
			}
		}

		return true;
	}

}
