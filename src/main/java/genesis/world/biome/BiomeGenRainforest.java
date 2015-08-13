package genesis.world.biome;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenGrowingPlant.GrowingPlantType;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

public class BiomeGenRainforest extends BiomeGenBaseGenesis
{
	public BiomeGenRainforest(int id)
	{
		super(id);
		setBiomeName("Rainforest");
		setTemperatureRainfall(0.95F, 1.0F);
		setHeight(-0.2F, 0.05F);
		
		theBiomeDecorator.grassPerChunk = 3;
		
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchSize(3).setCountPerChunk(3));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(3).setCountPerChunk(3));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setWaterProximity(1, 0).setNextToWater(true).setPlantType(GrowingPlantType.COLUMN).setPatchSize(4).setCountPerChunk(8));
		addDecoration(new WorldGenRockBoulders().setCountPerChunk(5));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		
		addTree(new WorldGenTreeLepidodendron(14, 18, true).setTreeCountPerChunk(9));
		addTree(new WorldGenTreeSigillaria(10, 15, true).setTreeCountPerChunk(5));
		addTree(new WorldGenTreePsaronius(5, 8, true).setTreeCountPerChunk(2));
		
		addTree(new WorldGenRottenLog(3, 6, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(5));
		addTree(new WorldGenRottenLog(3, 6, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(5));
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
