package genesis.world.biome;

import java.util.Random;

import genesis.world.gen.feature.WorldGenTreeArchaeopteris;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenArchaeopterisPlains extends BiomeGenBaseGenesis
{
	public BiomeGenArchaeopterisPlains (int id)
	{
		super(id);
		setBiomeName("Archaeopteris Plains");
		setTemperatureRainfall(1.15F, 1.0F);
		setHeight(new BiomeGenBase.Height(-0.1F, 0.01F));
		
		theBiomeDecorator.grassPerChunk = 0;
		
		addTree(new WorldGenTreeArchaeopteris(15, 25, true).setTreeCountPerChunk(1));
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
