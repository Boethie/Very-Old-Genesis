package genesis.world.biome.decorate;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PlantGenerator
{
	boolean placePlant(World world, BlockPos pos, Random rand);
}
