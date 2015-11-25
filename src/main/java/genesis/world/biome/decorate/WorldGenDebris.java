package genesis.world.biome.decorate;

import java.util.Random;

import genesis.common.GenesisBlocks;
import genesis.metadata.TreeBlocksAndItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenDebris extends WorldGenDecorationBase
{
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
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		int distanceX = 5;
		int distanceZ = 5;
		int distanceY = 3;
		
		IBlockState wood;
		
		found:
			for (int x = -distanceX; x <= distanceX; ++x)
			{
				for (int z = -distanceZ; z <= distanceZ; ++z)
				{
					for (int y = -distanceY; y <= distanceY; ++y)
					{
						wood = world.getBlockState(pos.add(x, y, z));
						
						if (GenesisBlocks.trees.isStateOf(wood, TreeBlocksAndItems.LOG))
						{
							break found;
						}
					}
				}
			}
		
		return true;
	}
}
