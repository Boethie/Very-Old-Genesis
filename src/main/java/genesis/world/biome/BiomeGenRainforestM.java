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

public class BiomeGenRainforestM extends BiomeGenRainforest
{
	public BiomeGenRainforestM(int id)
	{
		super(id);
		setBiomeName("Rainforest M");
		setHeight(0.4F, 1.1F);
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.odontopteris).setNextToWater(false).setPatchSize(3).setCountPerChunk(4));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.sphenophyllum).setPatchSize(4).setCountPerChunk(5));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.calamites).setWaterProximity(1, 0).setNextToWater(true).setPatchSize(4).setCountPerChunk(8));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(7));
		addDecoration(new WorldGenMossStages().setCountPerChunk(30));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeLepidodendron(14, 18, true).setTreeCountPerChunk(10));
		addTree(new WorldGenTreeSigillaria(10, 15, true).setTreeCountPerChunk(7));
		addTree(new WorldGenTreePsaronius(5, 8, true).setTreeCountPerChunk(3));
		
		addTree(new WorldGenRottenLog(3, 6, EnumTree.LEPIDODENDRON, true).setTreeCountPerChunk(5));
		addTree(new WorldGenRottenLog(3, 6, EnumTree.SIGILLARIA, true).setTreeCountPerChunk(4));
	}
}
