package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

public class BiomeGenRainforestEdge extends BiomeGenRainforest
{
	public BiomeGenRainforestEdge(int id)
	{
		super(id);
		setBiomeName("Rainforest Edge");
		
		theBiomeDecorator.grassPerChunk = 8;
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchSize(3).setCountPerChunk(4));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(3));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setWaterProximity(2, 0).setNextToWater(true).setPatchSize(4).setCountPerChunk(12));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(14));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
		
		addDecoration(new WorldGenRockBoulders().setRarity(80).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeLepidodendron(14, 16, true).setTreeCountPerChunk(5));
		addTree(new WorldGenTreeSigillaria(10, 12, true).setTreeCountPerChunk(3));
		addTree(new WorldGenTreePsaronius(5, 6, true).setTreeCountPerChunk(1));
		
		addTree(new WorldGenRottenLog(3, 5, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(2));
		addTree(new WorldGenRottenLog(3, 5, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(1));
	}
}
