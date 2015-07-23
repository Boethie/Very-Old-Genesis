package genesis.world.biome.decorate;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenPalaeoagaracites extends WorldGenDecorationBase
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
		
		return false;
	}
}
