package genesis.world.biome;

public class BiomeGenRainforestEdge extends BiomeGenRainforest
{

	public BiomeGenRainforestEdge(int id)
	{
		super(id);
		this.theBiomeDecorator.treesPerChunk = 5;
		this.biomeName = "Rainforest Edge";
	}

}
