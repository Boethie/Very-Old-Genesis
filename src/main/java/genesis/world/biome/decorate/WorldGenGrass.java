package genesis.world.biome.decorate;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class WorldGenGrass extends WorldGenerator
{
	public abstract IBlockState getSpawnablePlant(Random rand);
}
