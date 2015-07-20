package genesis.world.biome;

public class BiomeGenRainforestM extends BiomeGenRainforest
{
	public int totalTreesPerChunk = 500;
	
	public BiomeGenRainforestM(int id)
	{
		super(id);
		this.minHeight = 0.5F;
		this.maxHeight = 1.2F;
		this.biomeName = "Rainforest M";
	}
}
