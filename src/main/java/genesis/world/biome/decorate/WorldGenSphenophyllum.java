package genesis.world.biome.decorate;

import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSphenophyllum extends WorldGenDecorationBase
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
		
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		boolean water_exists = findBlockInRange(world, pos, Blocks.water.getDefaultState(), 4, 2, 4);
		/*
		found_water:
		for (int x = -4; x <= 4; ++x)
		{
			for (int z = -4; z <= 4; ++z)
			{
				for (int y = -2; y <= 2; ++y)
				{
					if (world.getBlockState(pos.add(x, y, z)) == Blocks.water.getDefaultState())
					{
						water_exists = true;
						break found_water;
					}
				}
			}
		}
		*/
		if (!water_exists)
			return false;
		
		placeRandomPlant(world, pos, random);
		
		BlockPos secondPos;
		
		int additional = random.nextInt(getPatchSize() - 1);
		
		for (int i = 0; i <= additional; ++i)
		{
			secondPos = pos.add(-3 + random.nextInt(7), 0, -3 + random.nextInt(7));
			if (
					world.getBlockState(secondPos).getBlock() == GenesisBlocks.moss
					|| world.getBlockState(secondPos).getBlock() == Blocks.dirt)
				placeRandomPlant(world, secondPos, random);
		}
		
		return true;
	}
	
	private void placeRandomPlant(World world, BlockPos pos, Random random)
	{
		int growth = random.nextInt(7);
		IBlockState bottom = GenesisBlocks.sphenophyllum.getDefaultState().withProperty(GenesisBlocks.sphenophyllum.ageProp, growth).withProperty(GenesisBlocks.sphenophyllum.topProp, false);
		IBlockState top = GenesisBlocks.sphenophyllum.getDefaultState().withProperty(GenesisBlocks.sphenophyllum.ageProp, growth).withProperty(GenesisBlocks.sphenophyllum.topProp, true);
		BlockPos placePos = pos.up();
		
		if (world.isAirBlock(placePos) && world.isAirBlock(placePos.up()))
		{
			world.setBlockState(placePos, bottom, 2);
			if (GenesisBlocks.sphenophyllum.growthAge <= growth)
			{
				world.setBlockState(placePos.up(), top, 2);
			}
		}
	}
}
