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
	private boolean nextToWater = false;
	private int waterRadius = 4;
	private int waterHeight = 2;
	private GrowingPlantType plantType = GrowingPlantType.NORMAL;
	
	public enum GrowingPlantType
	{
		NORMAL,
		DOUBLE,
		COLUMN
	}
	
	public WorldGenGrowingPlant(BlockGrowingPlant plant)
	{
		this.plant = plant;
	}
	
	public WorldGenGrowingPlant setPlantType(GrowingPlantType type)
	{
		this.plantType = type;
		return this;
	}
	
	public WorldGenGrowingPlant setWaterProximity(int radius, int height)
	{
		this.waterRadius = radius;
		this.waterHeight = height;
		return this;
	}
	
	public WorldGenGrowingPlant setNextToWater(boolean nextToWater)
	{
		this.nextToWater = nextToWater;
		return this;
	}
	
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
		
		boolean water_exists = findBlockInRange(world, pos, Blocks.water.getDefaultState(), waterRadius, waterHeight, waterRadius);
		
		if (!water_exists && nextToWater)
			return false;
		
		if (!world.getBlockState(pos.up()).getBlock().isAir(world, pos))
			return false;
		
		placeRandomPlant(world, pos, random);
		
		BlockPos secondPos;
		
		int additional = random.nextInt(getPatchSize() - 1);
		
		for (int i = 0; i <= additional; ++i)
		{
			secondPos = pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
			if (
					(world.getBlockState(secondPos).getBlock() == GenesisBlocks.moss
					|| world.getBlockState(secondPos).getBlock() == Blocks.dirt)
					&& (
							findBlockInRange(world, pos, Blocks.water.getDefaultState(), waterRadius, waterHeight, waterRadius)
							|| !nextToWater))
				placeRandomPlant(world, secondPos, random);
		}
		
		return true;
	}
	
	private boolean placeRandomPlant(World world, BlockPos pos, Random random)
	{
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		switch (plantType)
		{
		case DOUBLE:
			placePlantDouble(world, pos, random);
			break;
		case COLUMN:
			placePlantColumn(world, pos, random);
			break;
		default:
			placePlant(world, pos, random);
			break;
		}
		
		return true;
	}
	
	private void placePlantDouble(World world, BlockPos pos, Random random)
	{
		int growth = random.nextInt(7);
		
		IBlockState bottom = plant.getDefaultState().withProperty(plant.ageProp, growth).withProperty(plant.topProp, false);
		IBlockState top = plant.getDefaultState().withProperty(plant.ageProp, growth).withProperty(plant.topProp, true);
		
		BlockPos placePos = pos.up();
		
		if (world.isAirBlock(placePos) && world.isAirBlock(placePos.up()))
		{
			world.setBlockState(placePos, bottom, 2);
			if (plant.getGrowthAge() <= growth)
			{
				world.setBlockState(placePos.up(), top, 2);
			}
		}
	}
	
	private void placePlant(World world, BlockPos pos, Random random)
	{
		int growth = random.nextInt(7);
		IBlockState bottom = plant.getDefaultState().withProperty(plant.ageProp, growth);
		
		BlockPos placePos = pos.up();
		
		if (world.isAirBlock(placePos) && world.isAirBlock(placePos.up()))
		{
			world.setBlockState(placePos, bottom, 2);
		}
	}
	
	private void placePlantColumn(World world, BlockPos pos, Random random)
	{
		int height = 1 + random.nextInt(6);
		IBlockState plantBlock = plant.getDefaultState();
		
		BlockPos placePos = pos.up();
		
		for (int i = 0; i <= height; ++i)
			setBlockInWorld(world, placePos.add(0, i, 0), plantBlock);
	}
}
