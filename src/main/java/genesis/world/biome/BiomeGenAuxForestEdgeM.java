package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenArchaeomarasmius;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

public class BiomeGenAuxForestEdgeM extends BiomeGenAuxForestEdge
{
	public BiomeGenAuxForestEdgeM(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest Edge M");
		setHeight(0.4F, 0.9F);
		
		theBiomeDecorator.grassPerChunk = 3;
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenArchaeomarasmius().setPatchSize(3).setCountPerChunk(2));
		addDecoration(new WorldGenPalaeoagaracites().setPatchSize(16).setCountPerChunk(80));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPatchSize(5).setCountPerChunk(2));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(5));
		
		addDecoration(new WorldGenRockBoulders().setRarity(80).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeAraucarioxylon(22, 27, true).setTreeCountPerChunk(2));
		addTree(new WorldGenRottenLog(4, 8, EnumTree.ARAUCARIOXYLON, true).addTopDecoration(GenesisBlocks.archaeomarasmius.getDefaultState()).setTreeCountPerChunk(1));
	}
}
