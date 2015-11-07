package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenArchaeomarasmius;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

public class BiomeGenAuxForestEdge extends BiomeGenAuxForest
{
	public BiomeGenAuxForestEdge(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest Edge");
		
		theBiomeDecorator.grassPerChunk = 3;
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenArchaeomarasmius().setPatchSize(3).setCountPerChunk(3));
		addDecoration(new WorldGenPalaeoagaracites().setPatchSize(16).setCountPerChunk(80));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPatchSize(5).setCountPerChunk(2));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(5));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeAraucarioxylon(25, 27, true).setTreeCountPerChunk(2));
		addTree(new WorldGenRottenLog(3, 7, EnumTree.ARAUCARIOXYLON, true).addTopDecoration(GenesisBlocks.archaeomarasmius.getDefaultState()).setTreeCountPerChunk(1));
	}
}
