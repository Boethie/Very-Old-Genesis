package genesis.world.biome;

public class BiomeGenRainforest extends BiomeGenBaseGensis
{

	public BiomeGenRainforest(int id)
	{
		super(id);
		this.biomeName = "Rainforest";
		this.rainfall = 1.0F;
		this.minHeight = 0.05F;
		this.maxHeight = .1F;
		this.theBiomeDecorator.treesPerChunk = 10;
	}

}
