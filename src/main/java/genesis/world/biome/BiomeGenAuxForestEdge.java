package genesis.world.biome;

public class BiomeGenAuxForestEdge extends BiomeGenAuxForest
{
	public int totalTreesPerChunk = 300;
	
	public BiomeGenAuxForestEdge(int id)
	{
		super(id);
		this.theBiomeDecorator.treesPerChunk = 0;
		this.biomeName = "Araucarioxylon Forest Edge";
	}
}
