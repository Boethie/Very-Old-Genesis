package genesis.world.biome.decorate;

import java.util.Random;

import genesis.block.BlockGrowingPlant;
import genesis.common.GenesisBlocks;
import genesis.util.WorldUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
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
	protected boolean doGenerate(World world, Random random, BlockPos pos)
	{
		do
		{
			IBlockState state = world.getBlockState(pos);
			
			if (!state.getBlock().isAir(state, world, pos) && !state.getBlock().isLeaves(state, world, pos))
				break;
			
			pos = pos.down();
		}
		while (pos.getY() > 0);
		
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() != GenesisBlocks.moss && state.getBlock() != Blocks.dirt)
			return false;
		
		if (nextToWater && !WorldUtils.waterInRange(world, pos, waterRadius, waterRadius, waterHeight))
			return false;
		
		pos = pos.up();
		
		if (!world.isAirBlock(pos))
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
	
	protected boolean placeRandomPlant(World world, BlockPos pos, Random random)
	{
		plant.placeRandomAgePlant(world, pos, random);
		
		return true;
	}
}
