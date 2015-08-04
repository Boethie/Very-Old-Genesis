package genesis.world.biome;

import genesis.world.biome.decorate.WorldGenMossStages;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenSwamp extends BiomeGenBaseGenesis
{
	public BiomeGenSwamp(int id)
	{
		super(id);
		setBiomeName("Swamp");
		setHeight(-0.1F, 0.01F);
		
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
	}
	
	@Override
	public void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 1;
		mossStages[1] = 2;
		super.generateBiomeTerrain(world, rand, primer, blockX, blockZ, d);
	}
}
