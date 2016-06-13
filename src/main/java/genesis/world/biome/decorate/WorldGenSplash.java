/**
 * Created by Fatalitiii on 05/06/2016.
 */

package genesis.world.biome.decorate;

import java.util.Random;
import java.util.function.Function;

import genesis.block.BlockMoss;
import genesis.block.BlockMoss.EnumSoil;
import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSplash extends WorldGenDecorationBase
{
	public static WorldGenSplash createHumusSplash()
	{
		return (WorldGenSplash) new WorldGenSplash(
				(s, w, p) -> s.getBlock() == GenesisBlocks.moss,
				(s) -> s.withProperty(BlockMoss.SOIL, EnumSoil.HUMUS))
				.setDryRadius(-1).setPatchRadius(6);
	}
	
	protected final WorldBlockMatcher matcher;
	protected final Function<IBlockState, IBlockState> replacement;
	
	protected int dryRadius = 2;
	protected float centerChance = 0.5F;
	protected float edgeChance = 2;
	
	public WorldGenSplash(WorldBlockMatcher matcher, Function<IBlockState, IBlockState> replacement)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.SOLID_TOP);
		
		this.matcher = matcher;
		this.replacement = replacement;
		
		setPatchRadius(11);
		setPatchCount(64);
	}
	
	public WorldGenSplash(WorldBlockMatcher matcher, IBlockState replacement)
	{
		this(WorldBlockMatcher.or(matcher, WorldBlockMatcher.state(replacement)),
				(s) -> replacement);
	}
	
	public WorldGenSplash(IBlockState matchState, IBlockState replacement)
	{
		this(WorldBlockMatcher.state(matchState), replacement);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		pos = pos.down();
		
		// Check for water if necessary
		if (dryRadius != -1 && isMatchInSphere(world, pos, WorldBlockMatcher.WATER, dryRadius))
			return false;
		
		IBlockState state = world.getBlockState(pos);
		
		// Check for the state to replace
		if (matcher.apply(state, world, pos))
		{
			boolean surrounded = true;
			
			if (centerChance != edgeChance)
			{
				for (EnumFacing side : EnumFacing.HORIZONTALS)
				{
					IBlockState sideState = world.getBlockState(pos.offset(side));
					
					if (matcher.apply(sideState, world, pos))
					{
						surrounded = false;
						break;
					}
				}
			}
			
			state = replacement.apply(state);
			
			if (rand.nextFloat() <= (surrounded ? centerChance : edgeChance))
			{	// Set the block to the replacement state
				setBlock(world, pos, state);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 *
	 * @param radius set radius from water to stop spawning.
	 */
	public WorldGenSplash setDryRadius(int radius)
	{
		this.dryRadius = radius;
		return this;
	}
	
	/**
	 * @param chance Set chance of the block replacing the parent block.
	 */
	public WorldGenSplash setCenterChance(float chance)
	{
		this.centerChance = chance;
		return this;
	}
	
	/**
	 * @param chance Set chance of the block being placed next to the parent block.
	 */
	public WorldGenSplash setEdgeChance(float chance)
	{
		this.edgeChance = chance;
		return this;
	}
}
