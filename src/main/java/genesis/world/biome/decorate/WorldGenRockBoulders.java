package genesis.world.biome.decorate;

import genesis.combo.SiltBlocks;
import genesis.common.GenesisBlocks;
import genesis.util.WorldUtils;
import genesis.util.functional.WorldBlockMatcher;
import genesis.util.random.f.FloatRange;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.RandomIntProvider;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldGenRockBoulders extends WorldGenDecorationBase
{
	protected final IBlockState dry;
	protected final IBlockState wet;
	private boolean waterRequired = true;
	private boolean inGround = true;
	
	private FloatRange largeProvider;
	
	private RandomIntProvider smallCountProvider = IntRange.create(1, 3);
	private FloatRange smallProvider;
	
	private FloatRange horizStretch;
	private FloatRange vertStretch;
	
	public WorldGenRockBoulders(IBlockState dry, IBlockState wet)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER,
				(s, w, p) -> s.getBlock() == Blocks.dirt
						|| s.getBlock() == Blocks.grass
						|| s.getBlock() == GenesisBlocks.moss
						|| GenesisBlocks.silt.isStateOf(s, SiltBlocks.SILT));
		
		setRadius(1.25F);
		setStretch(1.25F);
		
		this.dry = dry;
		this.wet = wet;
	}
	
	public WorldGenRockBoulders(IBlockState dry)
	{
		this(dry, null);
	}
	
	public WorldGenRockBoulders()
	{
		this(GenesisBlocks.granite.getDefaultState(), GenesisBlocks.mossy_granite.getDefaultState());
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		if (waterRequired && !WorldUtils.waterInRange(world, pos, 1, 1, 1))
			return false;
		
		float radius = largeProvider.get(rand);
		
		double vx = pos.getY() + rand.nextDouble() + radius - 1;
		/*
		int height = world.getHeight(pos).getY();
		
		if(vx > height - 1)
			vx = height - (rand.nextBoolean() ? 1 : 2);
		*/
		Vec3d center = new Vec3d(
				pos.getX() + rand.nextDouble(),
				vx,
				pos.getZ() + rand.nextDouble());
		
		placeSphere(world, center, rand, radius);
		
		for (int i = smallCountProvider.get(rand); i > 0; i--)
		{
			Vec3d offset = new Vec3d(rand.nextDouble() - 0.5, (rand.nextDouble() - 0.5) * 0.5, rand.nextDouble() - 0.5).normalize();
			
			float smallRadius = smallProvider.get(rand);
			
			offset = offset.scale((radius + smallRadius) * MathHelper.getRandomDoubleInRange(rand, 0.5, 1) - 0.5);
			
			placeSphere(world, center.add(offset).addVector(0, smallRadius - 0.5, 0), rand, smallRadius);
		}
		
		return true;
	}
	
	protected void placeRock(World world, BlockPos pos, Random rand)
	{
		int water = 0;
		int solid = 0;
		
		if (wet != null)
		{
			for (EnumFacing side : EnumFacing.HORIZONTALS)
			{
				BlockPos sidePos = pos.offset(side);
				IBlockState state = world.getBlockState(sidePos);
				
				if (state.getMaterial() == Material.water)
					water++;
				else if (state.getBlock().isSideSolid(state, world, sidePos, side.getOpposite()))
					solid++;
			}
		}
		
		IBlockState state = dry;
		
		BlockPos abovePos = pos.up();
		IBlockState above = world.getBlockState(abovePos);
		
		if (wet != null)
		{
			if (water > 0
					&& !WorldUtils.isWater(world, pos.up())
					&& above.getMaterial() != Material.water
					&& !above.getBlock().isSideSolid(above, world, abovePos, EnumFacing.DOWN))
				state = wet;
			else if (solid + water < 4 && rand.nextInt(4) <= 2)	// 75% chance for moss.
				state = wet;
		}
		
		if (inGround)
			setBlock(world, pos, state);
		else
			setAirBlock(world, pos, state);
		
		//world.notifyBlockUpdate(pos, Blocks.air.getDefaultState(), dry, 3);	// For testing it with item right click.
	}
	
	protected void placeRockAndDryAround(World world, BlockPos pos, Random rand)
	{
		placeRock(world, pos, rand);
		
		if (wet != null)
		{
			for (EnumFacing side : EnumFacing.HORIZONTALS)
			{
				BlockPos sidePos = pos.offset(side);
				
				if (world.getBlockState(sidePos) == wet)
					placeRock(world, sidePos, rand);
			}
		}
	}
	
	protected void placeSphere(World world, Vec3d center, Random rand, float radius)
	{
		BlockPos pos = new BlockPos(center);
		int area = MathHelper.ceiling_float_int(radius);
		
		radius *= radius;
		
		float scaleX = horizStretch.get(rand);
		float scaleY = vertStretch.get(rand);
		float scaleZ = horizStretch.get(rand);
		
		for (BlockPos rockPos : BlockPos.getAllInBoxMutable(pos.add(-area, -area, -area), pos.add(area, area, area)))
		{
			double dX = (rockPos.getX() + 0.5 - center.xCoord) / scaleX;
			double dY = (rockPos.getY() + 0.5 - center.yCoord) / scaleY;
			double dZ = (rockPos.getZ() + 0.5 - center.zCoord) / scaleZ;
			
			if (dX * dX + dY * dY + dZ * dZ <= radius)
			{
				placeRockAndDryAround(world, rockPos, rand);
			}
		}
	}
	
	public WorldGenRockBoulders setInGround(boolean in)
	{
		inGround = in;
		return this;
	}
	
	public WorldGenRockBoulders setRadius(FloatRange large, FloatRange small)
	{
		largeProvider = large;
		smallProvider = small;
		return this;
	}
	
	public WorldGenRockBoulders setRadius(float large, float small)
	{
		return setRadius(FloatRange.create(1, large), FloatRange.create(0.5F, small));
	}
	
	public WorldGenRockBoulders setRadius(float radius)
	{
		return setRadius(radius, radius - 0.5F);
	}
	
	public WorldGenRockBoulders setStretch(FloatRange horizontal, FloatRange vertical)
	{
		horizStretch = horizontal;
		vertStretch = vertical;
		return this;
	}
	
	public WorldGenRockBoulders setStretch(float horizontal, float vertical)
	{
		return setStretch(FloatRange.create(horizontal - 0.5F, horizontal), FloatRange.create(vertical - 0.5F, vertical));
	}
	
	public WorldGenRockBoulders setStretch(float stretch)
	{
		return setStretch(stretch, stretch);
	}
	
	public WorldGenRockBoulders setWaterRequired(boolean required)
	{
		waterRequired = required;
		return this;
	}
}
