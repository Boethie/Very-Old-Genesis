package genesis.world.biome.decorate;

import genesis.block.BlockGrowingPlant;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenGrowingPlant extends WorldGenDecorationBase
{
	private BlockGrowingPlant plant;
	private boolean isDouble = true;
	
	public WorldGenGrowingPlant(BlockGrowingPlant plant)
	{
		this.plant = plant;
	}
	
	public WorldGenGrowingPlant setDouble(boolean isDouble)
	{
		this.isDouble = isDouble;
		return this;
	}
	
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
		IBlockState bottom;
		IBlockState top;
		
		if (isDouble)
		{
			bottom = plant.getDefaultState().withProperty(plant.ageProp, growth).withProperty(plant.topProp, false);
			top = plant.getDefaultState().withProperty(plant.ageProp, growth).withProperty(plant.topProp, true);
		}
		else
		{
			bottom = plant.getDefaultState().withProperty(plant.ageProp, growth);
			top = plant.getDefaultState().withProperty(plant.ageProp, growth);
		}
		
		BlockPos placePos = pos.up();
		
		if (world.isAirBlock(placePos) && world.isAirBlock(placePos.up()))
		{
			world.setBlockState(placePos, bottom, 2);
			if (plant.growthAge <= growth)
			{
				world.setBlockState(placePos.up(), top, 2);
			}
		}
	}
}
