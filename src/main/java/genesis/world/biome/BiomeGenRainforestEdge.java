package genesis.world.biome;

import genesis.world.biome.decorate.BiomeDecoratorGenesis;

public class BiomeGenRainforestEdge extends BiomeGenRainforest
{

	public BiomeGenRainforestEdge(int id)
	{
		super(id);
		this.theBiomeDecorator.treesPerChunk = 0;
		this.biomeName = "Rainforest Edge";
		((BiomeDecoratorGenesis) this.theBiomeDecorator).lepidodendtronPerChunk = 10;
	}

}
