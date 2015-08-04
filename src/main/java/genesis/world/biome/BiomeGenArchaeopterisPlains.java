package genesis.world.biome;

import genesis.world.gen.feature.WorldGenTreeArchaeopteris;

public class BiomeGenArchaeopterisPlains extends BiomeGenBaseGenesis
{
	public BiomeGenArchaeopterisPlains (int id)
	{
		super(id);
		setBiomeName("Archaeopteris Plains");
		setTemperatureRainfall(1.15F, 1.0F);
		setHeight(0.01F, 0.02F);
		theBiomeDecorator.grassPerChunk = 0;
		
		addTree(new WorldGenTreeArchaeopteris(15, 25, true).setTreeCountPerChunk(1));
	}
}
