package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumPlant;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenArchaeopterisPlains extends BiomeGenBaseGenesis
{
	public BiomeGenArchaeopterisPlains (int id)
	{
		super(id);
		setBiomeName("Archaeopteris Plains");
		setTemperatureRainfall(1.15F, 1.0F);
		setHeight(-0.1F, 0.01F);
		
		theBiomeDecorator.grassPerChunk = 3;
		
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		
		addTree(new WorldGenTreeArchaeopteris(15, 25, true).setTreeCountPerChunk(1));
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getBlockState(EnumPlant.PSILOPHYTON)).setVolume(64);
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
