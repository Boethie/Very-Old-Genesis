package genesis.world.biome.decorate;

import genesis.block.BlockGrowingPlant;
import genesis.util.WorldUtils;
import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenGrowingPlant extends WorldGenDecorationBase
{
	private BlockGrowingPlant plant;
	private boolean nextToWater = false;
	private int waterRadius = 4;
	private int waterHeight = 2;

	public WorldGenGrowingPlant(BlockGrowingPlant plant)
	{
		super(WorldBlockMatcher.STANDARD_AIR, WorldBlockMatcher.TRUE);

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
	public boolean place(World world, Random random, BlockPos pos) {
		return !(nextToWater && !WorldUtils.waterInRange(world, pos.down(), waterRadius, waterRadius, waterHeight)) &&
						plant.placeRandomAgePlant(world, pos, random, shouldNotify() ? 3 : 2);
	}
}
