package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumFern;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenArchaeomarasmius;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenAuxForest extends BiomeGenBaseGenesis
{
	public BiomeGenAuxForest(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest");
		setTemperatureRainfall(1.1F, 0.9F);
		setHeight(0.3F, 0.2F);
		
		theBiomeDecorator.grassPerChunk = 5;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenArchaeomarasmius().setPatchSize(3).setCountPerChunk(5));
		addDecoration(new WorldGenPalaeoagaracites().setPatchSize(16).setCountPerChunk(128));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPatchSize(5).setCountPerChunk(5));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(5));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenTreeAraucarioxylon(25, 30, true).setTreeCountPerChunk(5));
		addTree(new WorldGenRottenLog(3, 7, EnumTree.ARAUCARIOXYLON, true).addTopDecoration(GenesisBlocks.archaeomarasmius.getDefaultState()).setTreeCountPerChunk(3));
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getFernBlockState(EnumFern.PHLEBOPTERIS)).setVolume(64);
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
