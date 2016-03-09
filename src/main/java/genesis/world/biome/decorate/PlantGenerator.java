package genesis.world.biome.decorate;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface PlantGenerator
{
	public void placePlant(World world, BlockPos pos, Random rand);
}
