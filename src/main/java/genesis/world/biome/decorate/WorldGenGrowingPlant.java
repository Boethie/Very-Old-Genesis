package genesis.world.biome.decorate;

import java.util.Random;

import genesis.block.BlockGrowingPlant;
import genesis.util.WorldBlockMatcher;
import genesis.util.WorldUtils;

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
		super(WorldBlockMatcher.AIR_LEAVES, WorldBlockMatcher.TRUE);
		
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
		if (nextToWater && !WorldUtils.waterInRange(world, pos, waterRadius, waterRadius, waterHeight))
			return false;
		
		if (!world.isAirBlock(pos))
			return false;
		
		boolean success = placeRandomPlant(world, pos, random);
		
		BlockPos secondPos;
		
		int additional = random.nextInt(getPatchSize() - 1);
		
		for (int i = 0; i <= additional; ++i)
		{
			secondPos = pos.add(random.nextInt(7) - 3, 0, random.nextInt(7) - 3);
			
			if (!nextToWater || WorldUtils.waterInRange(world, pos, waterRadius, waterRadius, waterHeight))
				success |= placeRandomPlant(world, secondPos, random);
		}
		
		return success;
	}
	
	protected boolean placeRandomPlant(World world, BlockPos pos, Random random)
	{
		return plant.placeRandomAgePlant(world, pos, random);
	}
}
