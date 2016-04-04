package genesis.world.biome.decorate;

import java.util.Random;

import genesis.util.WorldBlockMatcher;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class WorldGenDecorationBase extends WorldGenerator
{
	private int countPerChunk = 0;
	private int patchSize = 0;
	
	protected int rarity = 1;
	
	protected final WorldBlockMatcher airMatcher;
	protected final WorldBlockMatcher groundMatcher;
	
	/**
	 * @param airMatcher Matcher to tell whether the generator should continue searching
	 * downward for the ground.
	 * @param groundMatcher Matcher to check whether the ground that is encountered
	 * is suitable for the generator.
	 */
	protected WorldGenDecorationBase(WorldBlockMatcher airMatcher, WorldBlockMatcher groundMatcher)
	{
		this.airMatcher = airMatcher;
		this.groundMatcher = groundMatcher;
	}
	
	/**
	 * Creates a generator that does not search for the ground below it.
	 */
	protected WorldGenDecorationBase()
	{
		this(null, null);
	}
	
	protected BlockPos findGround(IBlockAccess world, BlockPos pos)
	{
		if (airMatcher != null)
		{
			do
			{
				if (!airMatcher.apply(world, pos))
					break;
			} while ((pos = pos.down()).getY() >= 0);
		}
		
		if (groundMatcher != null && !groundMatcher.apply(world, pos))
			return null;
		
		return pos;
	}
	
	protected abstract boolean doGenerate(World world, Random rand, BlockPos pos);
	
	@Override
	public final boolean generate(World world, Random rand, BlockPos pos)
	{
		pos = findGround(world, pos);
		
		if (pos == null || rand.nextInt(rarity) != 0)
			return false;
		
		if (doGenerate(world, rand, pos.up()))
		{
			return true;
		}
		
		return false;
	}
	
	public WorldGenDecorationBase setCountPerChunk(int count)
	{
		countPerChunk = count;
		return this;
	}
	
	public int getCountPerChunk()
	{
		return countPerChunk;
	}
	
	public WorldGenDecorationBase setPatchSize(int size)
	{
		patchSize = size;
		return this;
	}
	
	public int getPatchSize()
	{
		return Math.max(patchSize, 1);
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
	
	protected void setBlockInWorld(World world, BlockPos pos, IBlockState state)
	{
		setBlockInWorld(world, pos, state, false);
	}
	
	protected void setBlockInWorld(World world, BlockPos pos, IBlockState state, boolean force)
	{
		boolean place = true;
		
		try
		{
			if (!(world.isAirBlock(pos)) && !force)
			{
				place = false;
			}
			
			if (place)
			{
				world.setBlockState(pos, state, 3);
			}
		}
		catch(Exception e)
		{
		}
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
