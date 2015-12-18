package genesis.world.biome;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumPlant;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenDebris;
import genesis.world.biome.decorate.WorldGenGrass;
import genesis.world.biome.decorate.WorldGenGrassMulti;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.biome.decorate.WorldGenRoots;
import genesis.world.gen.feature.WorldGenDeadLog;

public class BiomeGenMetaForest extends BiomeGenBaseGenesis
{
	public BiomeGenMetaForest(int id)
	{
		super(id);
		setBiomeName("Metasequoia Forest");
		setTemperatureRainfall(1.1F, 0.9F);
		setHeight(0.1F, 0.05F);
		
		theBiomeDecorator.grassPerChunk = 3;
		
		addDecorations();
		addTrees();
	}
	
	protected void addDecorations()
	{
		addDecoration(new WorldGenPlant(EnumPlant.MICROPETASOS).setCountPerChunk(1));
		addDecoration(new WorldGenPalaeoagaracites().setPatchSize(24).setCountPerChunk(128));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPatchSize(3).setCountPerChunk(2));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(3));
		addDecoration(new WorldGenDebris().setCountPerChunk(7));
		addDecoration(new WorldGenRoots().setCountPerChunk(32));
		
		addDecoration(new WorldGenRockBoulders().setRarity(110).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
	}
	
	protected void addTrees()
	{
		addTree(new WorldGenDeadLog(4, 8, EnumTree.METASEQUOIA, true).setTreeCountPerChunk(2));
	}
	
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getPlantBlockState(EnumPlant.CRETACIFILIX)).setVolume(64);
	}
	
	@Override
	public float getNightFogModifier()
	{
		return 0.65F;
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
