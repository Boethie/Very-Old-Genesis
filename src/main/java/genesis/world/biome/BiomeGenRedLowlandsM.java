package genesis.world.biome;

import genesis.world.gen.feature.WorldGenTreeVoltzia;

public class BiomeGenRedLowlandsM extends BiomeGenRedLowlands
{
	public BiomeGenRedLowlandsM(int id)
	{
		super(id);
		setBiomeName("Red Lowlands M");
		setHeight(0.4F, 0.6F);
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeVoltzia(5, 8, true).setTreeCountPerChunk(2).setRarity(1));
	}
}
