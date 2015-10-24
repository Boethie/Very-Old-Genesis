package genesis.world.biome.decorate;

import genesis.block.BlockGrowingPlant;
import genesis.common.GenesisBlocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenGrowingPlant extends WorldGenDecorationBase
{
	private BlockGrowingPlant plant;
	private boolean nextToWater = false;
	private int waterRadius = 4;
	private int waterHeight = 2;
	
	public WorldGenGrowingPlant(BlockGrowingPlant plant)
	{
		this.plant = plant;
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
							findBlockInRange(world, secondPos, Blocks.water.getDefaultState(), waterRadius, waterHeight, waterRadius)
							|| !nextToWater))
				placeRandomPlant(world, secondPos, random);
		}
		
		return true;
	}
	
	private boolean placeRandomPlant(World world, BlockPos pos, Random random)
	{
		if (!(world.getBlockState(pos).getBlock() == GenesisBlocks.moss || world.getBlockState(pos).getBlock() == Blocks.dirt))
			return false;
		
		plant.placeRandomAgePlant(world, pos.up(), random);
		
		return true;
	}
}
