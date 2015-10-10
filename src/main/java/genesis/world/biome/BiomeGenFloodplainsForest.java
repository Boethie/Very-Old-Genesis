package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumPlant;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenPlant;
import genesis.world.biome.decorate.WorldGenUnderWaterPatch;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeArchaeopteris;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenFloodplainsForest extends BiomeGenBaseGenesis
{
	public BiomeGenFloodplainsForest(int id)
	{
		super(id);
		setBiomeName("Floodplains Forest");
		setTemperatureRainfall(1.15F, 1.0F);
		setHeight(-0.2F, 0.03F);
		
		theBiomeDecorator.grassPerChunk = 0;
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(4));
		addDecoration(new WorldGenPlant(EnumPlant.PSILOPHYTON).setPatchSize(8).setCountPerChunk(6));
		addDecoration(new WorldGenUnderWaterPatch(GenesisBlocks.peat.getDefaultState()).setCountPerChunk(1));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		
		addTree(new WorldGenTreeArchaeopteris(15, 25, true).setCanGrowInWater(true).setTreeCountPerChunk(12));
		
		addTree(new WorldGenRottenLog(3, 6, EnumTree.ARCHAEOPTERIS, true).setCanGrowInWater(true).setTreeCountPerChunk(5));
	}
	/*
	@Override
	public WorldGenGrass getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenGrassMulti(GenesisBlocks.plants.getFernBlockState(EnumFern.RHACOPHYTON)).setVolume(64);
	}
	*/
	@Override
	public void generateBiomeTerrain(World world, Random rand, ChunkPrimer primer, int blockX, int blockZ, double d)
	{
		mossStages = new int[2];
		mossStages[0] = 0;
		mossStages[1] = 1;
		super.generateBiomeTerrain(world, rand, primer, blockX, blockZ, d);
	}
}
