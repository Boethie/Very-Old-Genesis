package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenPlant extends WorldGenDecorationBase
{
	private IBlockState plant;
	
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
		
		placePlant(world, pos, random);
		
		BlockPos secondPos;
		int additional = random.nextInt(getPatchSize() - 1);
		
		for (int i = 0; i <= additional; ++i)
		{
			secondPos = pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
			if (
					(world.getBlockState(secondPos).getBlock() == GenesisBlocks.moss
					|| world.getBlockState(secondPos).getBlock() == Blocks.dirt))
				placePlant(world, secondPos, random);
		}
		
		return true;
	}
	
	public WorldGenPlant setPlant(IBlockState blockPlant)
	{
		plant = blockPlant;
		return this;
	}
	
	private void placePlant(World world, BlockPos pos, Random random)
	{
		BlockPos placePos = pos.up();
		
		if (world.isAirBlock(placePos) && world.isAirBlock(placePos.up()))
		{
			world.setBlockState(placePos, plant, 2);
		}
	}
}
