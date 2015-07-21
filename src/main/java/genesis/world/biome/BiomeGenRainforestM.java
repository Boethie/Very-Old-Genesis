package genesis.world.biome;

public class BiomeGenRainforestM extends BiomeGenRainforest
{
	public int totalTreesPerChunk = 800;
	
	public BiomeGenRainforestM(int id)
	{
		super(id);
		this.biomeName = "Rainforest M";
		this.minHeight = 0.5F;
		this.maxHeight = 1.2F;
	}
}
