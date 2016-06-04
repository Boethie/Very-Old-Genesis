package genesis.world.biome.decorate;

import java.util.Random;

import genesis.util.functional.WorldBlockMatcher;
import genesis.util.random.i.IntRange;
import genesis.util.random.i.RandomIntProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
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
	
	protected BlockPos findGround(IBlockAccess world, BlockPos pos, int distance)
	{
		if (airMatcher != null)
		{
			if (!airMatcher.apply(world, pos.up()))
				return null;
			
			int down = 0;
			
			do
			{
				if (!airMatcher.apply(world, pos))
					break;
				
				if (distance != -1 && ++down >= distance)
					return null;
			} while ((pos = pos.down()).getY() >= 0);
		}
		
		if (groundMatcher != null && !groundMatcher.apply(world, pos))
			return null;
		
		return pos;
	}
	
	public abstract boolean place(World world, Random rand, BlockPos pos);
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		pos = findGround(world, pos, -1);
		
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
	
	public WorldGenDecorationBase setPatchRadius(double radius)
	{
		patchMaxRadius = radius;
		return this;
	}

	public WorldGenDecorationBase setPatchHeight(int height)
	{
		patchStartHeight = height;
		return this;
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
	
	public boolean findBlockInRange(World world, BlockPos pos, Block findWhat, int distanceX, int distanceY, int distanceZ)
	{
		for (int x = -distanceX; x <= distanceX; x++)
			for (int z = -distanceZ; z <= distanceZ; z++)
				for (int y = -distanceY; y <= distanceY; y++)
					if (world.getBlockState(pos.add(x, y, z)).getBlock() == findWhat)
						return true;
		
		return false;
	}
	
	public boolean findBlockInRange(World world, BlockPos pos, IBlockState findWhat, int distanceX, int distanceY, int distanceZ)
	{
		for (int x = -distanceX; x <= distanceX; x++)
			for (int z = -distanceZ; z <= distanceZ; z++)
				for (int y = -distanceY; y <= distanceY; y++)
					if (world.getBlockState(pos.add(x, y, z)) == findWhat)
						return true;
		
		return false;
	}
}
