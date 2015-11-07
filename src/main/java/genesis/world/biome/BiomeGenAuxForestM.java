package genesis.world.biome;

import genesis.common.GenesisBlocks;
import genesis.metadata.EnumTree;
import genesis.world.biome.decorate.WorldGenArchaeomarasmius;
import genesis.world.biome.decorate.WorldGenGrowingPlant;
import genesis.world.biome.decorate.WorldGenPalaeoagaracites;
import genesis.world.biome.decorate.WorldGenRockBoulders;
import genesis.world.gen.feature.WorldGenRottenLog;
import genesis.world.gen.feature.WorldGenTreeAraucarioxylon;

public class BiomeGenAuxForestM extends BiomeGenAuxForest
{
	public BiomeGenAuxForestM(int id)
	{
		super(id);
		setBiomeName("Araucarioxylon Forest M");
		setHeight(0.4F, 0.9F);
	}
	
	@Override
	protected void addDecorations()
	{
		addDecoration(new WorldGenArchaeomarasmius().setPatchSize(3).setCountPerChunk(3));
		addDecoration(new WorldGenPalaeoagaracites().setPatchSize(16).setCountPerChunk(80));
		addDecoration(new WorldGenGrowingPlant(GenesisBlocks.programinis).setPatchSize(5).setCountPerChunk(1));
		addDecoration(new WorldGenRockBoulders().setMaxHeight(4).setCountPerChunk(5));
		
		addDecoration(new WorldGenRockBoulders().setRarity(75).setWaterRequired(false).setMaxHeight(2).addBlocks(GenesisBlocks.octaedrite.getDefaultState()).setCountPerChunk(1));
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeAraucarioxylon(25, 30, true).setTreeCountPerChunk(5));
		addTree(new WorldGenRottenLog(3, 7, EnumTree.ARAUCARIOXYLON, true).addTopDecoration(GenesisBlocks.archaeomarasmius.getDefaultState()).setTreeCountPerChunk(2));
	}
}
