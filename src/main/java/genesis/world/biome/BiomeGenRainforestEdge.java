package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenMossStages;
import genesis.world.gen.feature.WorldGenDeadLog;
import genesis.world.gen.feature.WorldGenTreeLepidodendron;
import genesis.world.gen.feature.WorldGenTreePsaronius;
import genesis.world.gen.feature.WorldGenTreeSigillaria;

public class BiomeGenRainforestEdge extends BiomeGenRainforest
{
	public BiomeGenRainforestEdge(int id)
	{
		super(id);
		setBiomeName("Rainforest Edge");
		
		theBiomeDecorator.grassPerChunk = 5;
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchSize(3).setCountPerChunk(2));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(2));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setWaterProximity(2, 0).setNextToWater(true).setPatchSize(4).setCountPerChunk(12));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeLepidodendron(12, 16, true).setTreeCountPerChunk(10));
		addTree(new WorldGenTreeSigillaria(10, 13, true).setTreeCountPerChunk(1));
		addTree(new WorldGenTreePsaronius(5, 6, true).setTreeCountPerChunk(3));
		
		addTree(new WorldGenDeadLog(3, 6, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(5));
		addTree(new WorldGenDeadLog(3, 6, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(4));
	}
}
