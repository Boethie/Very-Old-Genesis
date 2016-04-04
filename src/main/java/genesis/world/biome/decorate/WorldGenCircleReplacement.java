package genesis.world.biome.decorate;

import java.util.Random;

import com.google.common.base.Predicate;

import genesis.util.random.i.IntRange;
import genesis.util.random.i.RandomIntProvider;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenCircleReplacement extends WorldGenerator
{
	protected final Predicate<IBlockState> centerPredicate;
	protected final RandomIntProvider radiusProvider;
	protected final RandomIntProvider heightProvider;
	protected final Predicate<IBlockState> replacePredicate;
	protected final IBlockState replacement;
	
	public WorldGenCircleReplacement(Predicate<IBlockState> centerPredicate,
			RandomIntProvider radiusProvider, RandomIntProvider heightProvider,
			Predicate<IBlockState> replacePredicate,
			IBlockState replacement)
	{
		this.centerPredicate = centerPredicate;
		this.radiusProvider = radiusProvider;
		this.heightProvider = heightProvider;
		this.replacePredicate = replacePredicate;
		this.replacement = replacement;
	}
	
	public WorldGenCircleReplacement(Predicate<IBlockState> centerPredicate,
			int radius, int height,
			Predicate<IBlockState> replacePredicate, IBlockState replacement)
	{
		this(centerPredicate, IntRange.create(radius - 2, radius), IntRange.create(height), replacePredicate, replacement);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		if (centerPredicate.apply(world.getBlockState(pos)))
			return false;
		
		int radius = radiusProvider.get(rand);
		int height = heightProvider.get(rand);
		
		for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++)
		{
			for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++)
			{
				int dX = x - pos.getX();
				int dZ = z - pos.getZ();
				
				if (dX * dX + dZ * dZ <= radius * radius)
				{
					for (int y = pos.getY() - height; y <= pos.getY() + height; y++)
					{
						BlockPos replacePos = new BlockPos(x, y, z);
						
						if (replacePredicate.apply(world.getBlockState(replacePos)))
							world.setBlockState(replacePos, replacement, 2);
					}
				}
			}
		}
		
		return true;
	}
}
