/**
 * Created by Fatalitiii on 05/06/2016.
 */

package genesis.world.biome.decorate;

import java.util.Random;

import genesis.util.functional.WorldBlockMatcher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSplash extends WorldGenDecorationBase
{
	protected final WorldBlockMatcher parentBlock;
	protected final IBlockState subBlock;
	
	protected int dryRadius = 2;
	protected float centerChance = 0.5F;
	protected float edgeChance = 2;
	
	public WorldGenSplash(IBlockState parentBlock, IBlockState subBlock)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.SOLID_TOP);
		
		this.parentBlock = WorldBlockMatcher.state(parentBlock);
		this.subBlock = subBlock;
		
		setPatchRadius(4);
		setPatchCount(64);
	}
	
	public WorldGenSplash(WorldBlockMatcher parentBlock, IBlockState subBlock)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.SOLID_TOP);
		
		this.parentBlock = parentBlock;
		this.subBlock = subBlock;
		
		setPatchRadius(4);
		setPatchCount(64);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		pos = pos.down();
		
		//WATER CHECK
		if (dryRadius != -1 && isMatchInSphere(world, pos, WorldBlockMatcher.WATER, dryRadius))
			return false;
		
		//PARENT BELOW CHECK
		if (parentBlock.apply(world, pos))
		{
			boolean surrounded = true;
			
			if (centerChance != edgeChance)
			{
				for (EnumFacing side : EnumFacing.HORIZONTALS)
				{
					IBlockState state = world.getBlockState(pos.offset(side));
					
					if (state != parentBlock && state != subBlock)
					{
						surrounded = false;
						break;
					}
				}
			}
			
			if (rand.nextFloat() <= (surrounded ? centerChance : edgeChance))
			{
				setBlock(world, pos, subBlock);
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
