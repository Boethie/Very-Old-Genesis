package genesis.world.biome;

import genesis.world.gen.feature.WorldGenTreeVoltzia;

public class BiomeGenRedDesertM extends BiomeGenRedDesert
{
	public BiomeGenRedDesertM(int id)
	{
		super(id);
		setBiomeName("Red Desert M");
		setHeight(0.4F, 0.7F);
	}
	
	@Override
	protected void addTrees()
	{
		addTree(new WorldGenTreeVoltzia(5, 8, true).setTreeCountPerChunk(1).setRarity(2));
	}
}
