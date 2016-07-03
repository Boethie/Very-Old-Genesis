package genesis.world.biome.decorate;

import java.util.Random;

import genesis.common.Genesis;
import genesis.util.functional.WorldBlockMatcher;
import genesis.util.math.PosVecIterable;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.RandomIntProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class WorldGenDecorationBase extends WorldGenerator
{
	private final boolean notify;
	
	private int rarity = 1;
	
	protected final WorldBlockMatcher airMatcher;
	protected final WorldBlockMatcher groundMatcher;
	
	private RandomIntProvider patchCountProvider = IntRange.create(1);
	private double patchMaxRadius = 3;
	private int patchStartHeight = 1;
	
	/**
	 * @param airMatcher Matcher to tell whether the generator should continue searching
	 * downward for the ground.
	 * @param groundMatcher Matcher to check whether the ground that is encountered
	 * is suitable for the generator.
	 */
	protected WorldGenDecorationBase(WorldBlockMatcher airMatcher, WorldBlockMatcher groundMatcher, boolean notify)
	{
		super(notify);
		
		this.airMatcher = airMatcher;
		this.groundMatcher = groundMatcher;
		this.notify = notify;
	}
	
	protected WorldGenDecorationBase(WorldBlockMatcher airMatcher, WorldBlockMatcher groundMatcher)
	{
		this(airMatcher, groundMatcher, false);
	}
	
	/**
	 * Creates a generator that does not search for the ground below it.
	 */
	protected WorldGenDecorationBase(boolean notify)
	{
		this(null, null, notify);
	}
	
	/**
	 * Creates a generator that does not search for the ground below it.
	 */
	protected WorldGenDecorationBase()
	{
		this(false);
	}
	
	protected boolean shouldNotify()
	{
		return notify;
	}
	
	protected BlockPos findGround(IBlockAccess world, BlockPos pos, Random rand, int distance)
	{
		if (airMatcher != null)
		{
			pos = pos.up();
			
			if (!airMatcher.apply(world, pos))
				return null;
			
			for (MutableBlockPos mutPos : new PosVecIterable(pos, EnumFacing.DOWN, distance))
			{
				if (!airMatcher.apply(world, mutPos))
				{
					pos = mutPos.toImmutable();
					break;
				}
			}
		}
		
		if (groundMatcher != null && !groundMatcher.apply(world, pos))
			return null;
		
		return pos;
	}
	
	public abstract boolean place(World world, Random rand, BlockPos pos);
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		pos = findGround(world, pos, rand, -1);
		
		if (pos == null || rand.nextInt(rarity) != 0)
			return false;
		
		boolean success = false;
		int count = patchCountProvider.get(rand);
		
		for (int i = 0; i < count; i++)
		{
			BlockPos genPos = pos;
			
			if (i != 0)
			{
				Vec3d offset = new Vec3d(rand.nextDouble() - 0.5, 0, rand.nextDouble() - 0.5)
						.normalize()
						.scale(rand.nextDouble() * patchMaxRadius);
				genPos = findGround(world,
						pos.add(offset.xCoord, offset.yCoord + patchStartHeight, offset.zCoord),
						rand,
						patchStartHeight * 2 + 1);
			}
			
			if (genPos != null && place(world, rand, genPos.up()))
				success = true;
		}
		
		return success;
	}
	
	public WorldGenDecorationBase setPatchCount(RandomIntProvider count)
	{
		patchCountProvider = count;
		return this;
	}
	
	public WorldGenDecorationBase setPatchCount(int min, int max)
	{
		return setPatchCount(IntRange.create(min, max));
	}
	
	public WorldGenDecorationBase setPatchCount(int count)
	{
		return setPatchCount(count / 2, count);
	}
	
	protected RandomIntProvider getPatchCountProvider()
	{
		return patchCountProvider;
	}

	public WorldGenDecorationBase setPatchRadius(double radius)
	{
		if (radius > 8)
			Genesis.logger.error("Patch radius exceeded 8", new IllegalArgumentException());
		
		patchMaxRadius = radius;
		return this;
	}

	protected double getPatchMaxRadius()
	{
		return patchMaxRadius;
	}

	public WorldGenDecorationBase setPatchHeight(int height)
	{
		patchStartHeight = height;
		return this;
	}
	
	protected int getPatchStartHeight()
	{
		return patchStartHeight;
	}

	public WorldGenDecorationBase setRarity(int rarity)
	{
		this.rarity = rarity;
		return this;
	}
	
	public int getRarity()
	{
		return this.rarity;
	}
	
	protected void setBlock(World world, BlockPos pos, IBlockState state)
	{
		setBlockAndNotifyAdequately(world, pos, state);
	}
	
	protected boolean setAirBlock(World world, BlockPos pos, IBlockState state)
	{
		IBlockState stateAt = world.getBlockState(pos);
		
		if (stateAt.getBlock().isAir(stateAt, world, pos))
		{
			setBlock(world, pos, state);
			return true;
		}
		
		return false;
	}
	
	protected boolean setReplaceableBlock(World world, BlockPos pos, IBlockState state)
	{
		if (world.getBlockState(pos).getBlock().isReplaceable(world, pos))
		{
			setBlock(world, pos, state);
			return true;
		}
		
		return false;
	}
}
