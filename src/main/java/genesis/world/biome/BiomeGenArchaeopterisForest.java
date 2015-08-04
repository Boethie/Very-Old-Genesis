package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumPlant;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenArchaeopterisForest extends BiomeGenBaseGenesis
{
	public BiomeGenArchaeopterisForest(int id)
	{
		super(id);
		setBiomeName("Archaeopteris Forest");
		setTemperatureRainfall(1.15F, 1.0F);
		setHeight(new BiomeGenBase.Height(-0.1F, 0.01F));
		
		theBiomeDecorator.grassPerChunk = 1;
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(3).setCountPerChunk(3));
		
		addTree(new WorldGenTreeArchaeopteris(15, 20, true).setTreeCountPerChunk(10));
		addTree(new WorldGenRottenLog(3, 6, EnumTree.ARCHAEOPTERIS, true).setTreeCountPerChunk(5));
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
