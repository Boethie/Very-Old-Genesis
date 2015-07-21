package genesis.world.biome;

public class BiomeGenAuxForestM extends BiomeGenAuxForest
{
	public int totalTreesPerChunk = 250;
	
	public BiomeGenAuxForestM(int id)
	{
		super(id);
		this.biomeName = "Araucarioxylon Forest M";
		this.minHeight = 0.8F;
		this.maxHeight = 1.7F;
	}
}
