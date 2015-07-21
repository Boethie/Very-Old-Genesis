package genesis.world.biome;

public class BiomeGenAuxForestEdgeM extends BiomeGenAuxForestEdge
{
	public int totalTreesPerChunk = 250;
	
	public BiomeGenAuxForestEdgeM(int id)
	{
		super(id);
		this.biomeName = "Araucarioxylon Forest Edge M";
		this.minHeight = 0.9F;
		this.maxHeight = 1.9F;
	}
}
