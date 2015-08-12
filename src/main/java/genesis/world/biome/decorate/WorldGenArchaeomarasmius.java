package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenArchaeomarasmius extends WorldGenDecorationBase
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
		
		int plantsPlaced = 0;
		
		int patch = random.nextInt(getPatchSize());
		
		if (patch == 0)
			patch = 1;
		
		for (int i = 1; i <= patch; ++i)
		{
			if (placePlant(world, pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) -3)))
				++plantsPlaced;
		}
		
		if (plantsPlaced == 0)
			return false;
		
		return true;
	}
	
	private boolean placePlant(World world, BlockPos pos)
	{
		if (
				world.getBlockState(pos) == null
				|| !(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt)
				|| world.getLight(pos.up()) > 14)
			return false;
		
		setBlockInWorld(world, pos.up(), GenesisBlocks.archaeomarasmius.getDefaultState());
		
		return true;
	}

	@Override
	public IBlockState getSpawnablePlant(Random rand)
	{
		return GenesisBlocks.archaeomarasmius.getDefaultState();
	}
}
