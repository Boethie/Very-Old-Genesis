package genesis.world.biome.decorate;

import java.util.Random;
import java.util.function.Predicate;

import genesis.common.GenesisBlocks;
import genesis.util.functional.WorldBlockMatcher;
import genesis.util.random.f.FloatRange;
import genesis.util.random.i.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenCircleReplacement extends WorldGenDecorationBase
{
	public static WorldGenCircleReplacement getPeatGen()
	{
		return new WorldGenCircleReplacement((s) -> s.getMaterial() == Material.WATER,
				FloatRange.create(2, 3), IntRange.create(1),
				(s) -> s.getBlock() == Blocks.DIRT, GenesisBlocks.peat.getDefaultState());
	}
	
	protected final Predicate<IBlockState> centerPredicate;
	protected final FloatRange radiusProvider;
	protected final RandomIntProvider heightProvider;
	protected final Predicate<IBlockState> replacePredicate;
	protected final IBlockState replacement;
	
	public WorldGenCircleReplacement(Predicate<IBlockState> centerPredicate,
			FloatRange radiusProvider, RandomIntProvider heightProvider,
			Predicate<IBlockState> replacePredicate,
			IBlockState replacement)
	{
		super(WorldBlockMatcher.STANDARD_AIR_WATER, WorldBlockMatcher.TRUE);
		
		this.centerPredicate = centerPredicate;
		this.radiusProvider = radiusProvider;
		this.heightProvider = heightProvider;
		this.replacePredicate = replacePredicate;
		this.replacement = replacement;
	}
	
	public WorldGenCircleReplacement(Predicate<IBlockState> centerPredicate,
			float radius, int height,
			Predicate<IBlockState> replacePredicate, IBlockState replacement)
	{
		this(centerPredicate, FloatRange.create(radius - 2, radius), IntRange.create(height), replacePredicate, replacement);
	}
	
	@Override
	public boolean place(World world, Random rand, BlockPos pos)
	{
		if (!centerPredicate.test(world.getBlockState(pos)))
			return false;
		
		float radius = radiusProvider.get(rand);
		int height = heightProvider.get(rand);
		
		pos = pos.down();
		
		int horizArea = MathHelper.ceiling_float_int(radius);
		
		for (int x = pos.getX() - horizArea; x <= pos.getX() + horizArea; x++)
		{
			for (int z = pos.getZ() - horizArea; z <= pos.getZ() + horizArea; z++)
			{
				int dX = x - pos.getX();
				int dZ = z - pos.getZ();
				
				if (dX * dX + dZ * dZ <= radius * radius)
				{
					for (int y = pos.getY() - height; y <= pos.getY() + height; y++)
					{
						BlockPos replacePos = new BlockPos(x, y, z);
						
						if (replacePredicate.test(world.getBlockState(replacePos)))
							world.setBlockState(replacePos, replacement, 3);
					}
				}
			}
		}
		
		return true;
	}
}
